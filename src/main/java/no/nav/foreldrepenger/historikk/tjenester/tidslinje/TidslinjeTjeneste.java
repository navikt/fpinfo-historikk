package no.nav.foreldrepenger.historikk.tjenester.tidslinje;

import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.common.innsyn.v2.Arbeidsgiver;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivDokument;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag;
import no.nav.foreldrepenger.historikk.tjenester.innsending.Innsending;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.Inntektsmelding;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingInnslag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Stream.concat;

@ConditionalOnNotProd
@Service
public class TidslinjeTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(TidslinjeTjeneste.class);

    private final Inntektsmelding inntektsmelding;
    private final Innsending innsending;

    private final ArkivTjeneste arkivTjeneste;

    public TidslinjeTjeneste(Innsending innsending, Inntektsmelding inntektsmelding, ArkivTjeneste arkivTjeneste) {
        this.innsending = innsending;
        this.inntektsmelding = inntektsmelding;
        this.arkivTjeneste = arkivTjeneste;
    }

    public List<TidslinjeHendelse> tidslinje(String saksnummer) {
        var docsByJournalpost = arkivTjeneste.hentDokumentoversikt()
            .stream()
            .filter(dok -> saksnummer.equals(dok.saksnummer()))
            .collect(Collectors.groupingBy(ArkivDokument::journalpost));
        return concat(inntektsmelding.inntektsmeldinger().stream(), innsending.innsendinger().stream())
            .peek(h -> {
                if (h.getSaksnr() == null) {
                    LOG.info("Historikkinnslag {} uten saksnummer", h.getReferanseId());
                }
            })
            .filter(i -> saksnummer.equals(i.getSaksnr()))
            .sorted()
            .map(hi -> mapTil(hi, docsByJournalpost))
            .collect(Collectors.toList());
    }

    private TidslinjeHendelse mapTil(HistorikkInnslag h,
                                     Map<String, List<ArkivDokument>> docsByJournalpost) {
        return switch (h) {
            case InnsendingInnslag ii -> mapTil(ii, docsByJournalpost);
            case InntektsmeldingInnslag im -> mapTil(im, docsByJournalpost);
            default -> throw new IllegalStateException("Ikke implementert for: " + h);
        };
    }

    private InntektsmeldingHendelse mapTil(InntektsmeldingInnslag ims,
                                           Map<String, List<ArkivDokument>> docsByJournalpost) {
        var dokumenter = docsByJournalpost.get(ims.getJournalpostId());
        var arbeidsgiverType = ims.getArbeidsgiver().getId().length() != 11
            ? Arbeidsgiver.ArbeidsgiverType.ORGANISASJON
            : Arbeidsgiver.ArbeidsgiverType.PRIVAT;
        var arbeidsgiver = new Arbeidsgiver(ims.getArbeidsgiver().getId(), arbeidsgiverType);
        return InntektsmeldingHendelse.builder()
            .arbeidsgiver(arbeidsgiver)
            .dokumenter(dokumenter)
            .tidslinjeHendelseType(TidslinjeHendelseType.INNTEKTSMELDING)
            .build();
    }

    private TidslinjeHendelse mapTil(InnsendingInnslag h,
                                     Map<String, List<ArkivDokument>> docsByJournalpost) {
        var dokumenter = docsByJournalpost.get(h.getJournalpostId());
        if (h.getHendelse().erInitiellSøknad()) {
            return søknad(h, dokumenter);
        } else if (h.getHendelse().erEndringssøknad()) {
            return endringssøknad(h, dokumenter);
        } else if (h.getHendelse().erEttersending()) {
            return ettersending(h, dokumenter);
        } else {
            throw new IllegalStateException("Nådde feil sted gitt");
        }
    }

    private TidslinjeHendelse endringssøknad(InnsendingInnslag h,
                                             List<ArkivDokument> dokumenter) {
        return Søknadshendelse.builder()
            .tidslinjeHendelseType(TidslinjeHendelseType.ENDRINGSSØKNAD)
            .aktørType(AktørType.BRUKER)
            .dokumenter(dokumenter)
            .opprettet(h.getInnsendt())
            .build();
    }

    private TidslinjeHendelse søknad(InnsendingInnslag h,
                                     List<ArkivDokument> dokumenter) {
        return Søknadshendelse.builder()
            .manglendeVedlegg(h.getIkkeOpplastedeVedlegg())
            .aktørType(AktørType.BRUKER)
            .dokumenter(dokumenter)
            .opprettet(h.getInnsendt())
            .tidslinjeHendelseType(TidslinjeHendelseType.FØRSTEGANGSSØKNAD)
            .build();
    }

    private TidslinjeHendelse ettersending(InnsendingInnslag h,
                                           List<ArkivDokument> dokumenter) {
        if (dokumenter.isEmpty()) {
            throw new IllegalStateException("Fant ikke journalpost på ettersending");
        }
        return EttersendingHendelse.builder()
            .dokumenter(List.of())
            .aktørType(AktørType.BRUKER)
            .dokumenter(dokumenter)
            .opprettet(h.getInnsendt())
            .tidslinjeHendelseType(TidslinjeHendelseType.ETTERSENDING)
            .build();
    }
}
