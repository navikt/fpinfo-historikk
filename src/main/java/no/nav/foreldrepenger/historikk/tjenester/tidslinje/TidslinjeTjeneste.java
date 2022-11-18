package no.nav.foreldrepenger.historikk.tjenester.tidslinje;

import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.common.innsyn.v2.Arbeidsgiver;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag;
import no.nav.foreldrepenger.historikk.tjenester.innsending.Innsending;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.Inntektsmelding;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingInnslag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Stream.concat;

@ConditionalOnNotProd
@Service
public class TidslinjeTjeneste {

    private final Inntektsmelding inntektsmelding;
    private final Innsending innsending;

    public TidslinjeTjeneste(Innsending innsending, Inntektsmelding inntektsmelding) {
        this.innsending = innsending;
        this.inntektsmelding = inntektsmelding;
    }

    public List<TidslinjeHendelse> tidslinje(String saksnummer) {
        return concat(inntektsmelding.inntektsmeldinger().stream(), innsending.innsendinger().stream())
            .filter(i -> i.getSaksnr().equals(saksnummer))
            .sorted()
            .map(this::mapTil)
            .collect(Collectors.toList());
    }

    private TidslinjeHendelse mapTil(HistorikkInnslag h) {
        return switch (h) {
            case InnsendingInnslag ii -> mapTil(ii);
            case InntektsmeldingInnslag im -> mapTil(im);
            default -> throw new IllegalStateException("Unexpected value: " + h);
        };
    }

    private InntektsmeldingHendelse mapTil(InntektsmeldingInnslag ims) {
        var arbeidsgiverType = ims.getArbeidsgiver().getId().length() != 11
            ? Arbeidsgiver.ArbeidsgiverType.ORGANISASJON
            : Arbeidsgiver.ArbeidsgiverType.PRIVAT;
        var arbeidsgiver = new Arbeidsgiver(ims.getArbeidsgiver().getId(), arbeidsgiverType);
        return InntektsmeldingHendelse.builder()
            .arbeidsgiver(arbeidsgiver)
            .tidslinjeHendelseType(TidslinjeHendelseType.INNTEKTSMELDING)
            .build();
    }

    private TidslinjeHendelse mapTil(InnsendingInnslag h) {
        if (h.getHendelse().erInitiellSøknad()) {
            return søknad(h);
        } else if (h.getHendelse().erEndringssøknad()) {
            return endringssøknad(h);
        } else if (h.getHendelse().erEttersending()) {
            return ettersending(h);
        } else {
            throw new IllegalStateException("Nådde feil sted gitt");
        }
    }

    private TidslinjeHendelse endringssøknad(InnsendingInnslag h) {
        return Søknadshendelse.builder()
            .tidslinjeHendelseType(TidslinjeHendelseType.ENDRINGSSØKNAD)
            .aktørType(AktørType.BRUKER)
            .opprettet(h.getInnsendt())
            .build();
    }

    private TidslinjeHendelse søknad(InnsendingInnslag h) {
        return Søknadshendelse.builder()
            .eksempelfelt("HUHEI")
            .aktørType(AktørType.BRUKER)
            .opprettet(h.getInnsendt())
            .tidslinjeHendelseType(TidslinjeHendelseType.FØRSTEGANGSSØKNAD)
            .build();
    }

    private TidslinjeHendelse ettersending(InnsendingInnslag h) {
        if (h.getJournalpostId() == null) {
            throw new IllegalStateException("Fant ikke journalpost på ettersending");
        }
        return EttersendingHendelse.builder()
            .dokumenter(List.of())
            .aktørType(AktørType.BRUKER)
            .opprettet(h.getInnsendt())
            .tidslinjeHendelseType(TidslinjeHendelseType.ETTERSENDING)
            .build();
    }
}
