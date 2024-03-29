package no.nav.foreldrepenger.historikk.tjenester.tidslinje;

import no.nav.foreldrepenger.common.innsyn.Arbeidsgiver;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivDokument;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.DokumentType;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingInnslag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

public class TidslinjeMapper {

    private static final Logger LOG = LoggerFactory.getLogger(TidslinjeMapper.class);

    private TidslinjeMapper() {

    }

    public static List<TidslinjeHendelse> map(List<HistorikkInnslag> innslag, List<ArkivDokument> dokumenter) {
        var dokumenterPerJournalpost = dokumenter.stream().collect(Collectors.groupingBy(ArkivDokument::journalpost));
        var vedtakHendelser = vedtakHendelserFra(dokumenter);
        var dokumentHendelser = dokumentHendelserFra(dokumenter);
        var innslagHendelser = innslag.stream().map(h -> map(h, dokumenterPerJournalpost, innslag));
        return concat(concat(vedtakHendelser, innslagHendelser), dokumentHendelser)
            .sorted()
            .collect(Collectors.toList());
    }

    private static Stream<TidslinjeHendelse> dokumentHendelserFra(List<ArkivDokument> dokumenter) {
        return dokumenter.stream()
            .filter(dok -> dok.brevkode() != null && (dok.brevkode().erInnhentOpplysningerbrev() || dok.brevkode().erEtterlysInntektsmeldingbrev()))
            .map(dok -> UtgåendeDokumentHendelse.builder()
                                            .tidslinjeHendelseType(dok.brevkode().erInnhentOpplysningerbrev()
                                                ? TidslinjeHendelseType.UTGÅENDE_INNHENT_OPPLYSNINGER
                                                : TidslinjeHendelseType.UTGÅENDE_ETTERLYS_INNTEKTSMELDING)
                                            .dokumenter(List.of(dok))
                                            .aktørType(AktørType.NAV)
                                            .opprettet(dok.mottatt())
                                            .build());
    }

    private static Stream<TidslinjeHendelse> vedtakHendelserFra(List<ArkivDokument> dokumenter) {
        return dokumenter.stream()
                         .filter(dok -> dok.type() == DokumentType.UTGÅENDE_DOKUMENT)
                         .peek(dok -> LOG.info("Utgående dokument: {}", dok))
                         .filter(dok -> !Objects.isNull(dok.brevkode()))
                         .filter(dok -> dok.brevkode().erVedtaksbrev())
                         .map(dok -> VedtakHendelse.builder()
                                               .tidslinjeHendelseType(TidslinjeHendelseType.VEDTAK)
                                               .dokumenter(List.of(dok))
                                               .aktørType(AktørType.NAV)
                                               .opprettet(dok.mottatt())
                                               .vedtakType(VedtakHendelse.VedtakType.INNVILGELSE) // TODO: sjekk type
                                               .build());
    }

    public static TidslinjeHendelse map(HistorikkInnslag h,
                                        Map<String, List<ArkivDokument>> docsByJournalpost,
                                        List<HistorikkInnslag> innslag) {
        return switch (h) {
            case InnsendingInnslag ii -> mapTil(ii, docsByJournalpost, innslag);
            case InntektsmeldingInnslag im -> mapTil(im, docsByJournalpost);
            default -> throw new IllegalStateException("Ikke implementert for: " + h);
        };
    }

    private static InntektsmeldingHendelse mapTil(InntektsmeldingInnslag ims,
                                                  Map<String, List<ArkivDokument>> docsByJournalpost) {
        var dokumenter = docsByJournalpost.get(ims.getJournalpostId());
        var arbeidsgiverType = ims.getArbeidsgiver().getId().length() != 11
            ? Arbeidsgiver.ArbeidsgiverType.ORGANISASJON
            : Arbeidsgiver.ArbeidsgiverType.PRIVAT;
        var arbeidsgiver = new Arbeidsgiver(ims.getArbeidsgiver().getId(), arbeidsgiverType);
        return InntektsmeldingHendelse.builder()
                                      .arbeidsgiver(arbeidsgiver)
                                      .aktørType(AktørType.ARBEIDSGIVER)
                                      .dokumenter(dokumenter)
                                      .opprettet(firstNonNull(ims.getInnsendt(), ims.getOpprettet()))
                                      .tidslinjeHendelseType(TidslinjeHendelseType.INNTEKTSMELDING)
                                      .build();
    }

    private static TidslinjeHendelse mapTil(InnsendingInnslag h,
                                            Map<String, List<ArkivDokument>> docsByJournalpost,
                                            List<HistorikkInnslag> innslag) {
        var dokumenter = docsByJournalpost.get(h.getJournalpostId());
        if (h.getHendelse().erInitiellSøknad()) {
            return søknad(h, dokumenter, innslag);
        } else if (h.getHendelse().erEndringssøknad()) {
            return endringssøknad(h, dokumenter);
        } else if (h.getHendelse().erEttersending()) {
            return ettersending(h, dokumenter);
        } else {
            throw new IllegalStateException("Innsendinginnslag ikke kjent");
        }
    }

    private static TidslinjeHendelse endringssøknad(InnsendingInnslag h,
                                                    List<ArkivDokument> dokumenter) {
        return Søknadshendelse.builder()
                              .tidslinjeHendelseType(TidslinjeHendelseType.ENDRINGSSØKNAD)
                              .aktørType(AktørType.BRUKER)
                              .dokumenter(dokumenter)
                              .opprettet(firstNonNull(h.getInnsendt(), h.getOpprettet()))
                              .build();
    }

    private static TidslinjeHendelse søknad(InnsendingInnslag h,
                                            List<ArkivDokument> dokumenter,
                                            List<HistorikkInnslag> innslag) {
        var nyførstegang = nyførstegang(innslag, h);
        var hendelseType = nyførstegang
            ? TidslinjeHendelseType.FØRSTEGANGSSØKNAD_NY
            : TidslinjeHendelseType.FØRSTEGANGSSØKNAD;
        return Søknadshendelse.builder()
                              .manglendeVedlegg(h.getIkkeOpplastedeVedlegg())
                              .aktørType(AktørType.BRUKER)
                              .dokumenter(dokumenter)
                              .opprettet(firstNonNull(h.getInnsendt(), h.getOpprettet()))
                              .tidslinjeHendelseType(hendelseType)
                              .build();
    }

    private static boolean nyførstegang(List<HistorikkInnslag> innslag, InnsendingInnslag h) {
        return innslag.stream()
            .filter(hi -> hi instanceof InnsendingInnslag)
            .filter(i -> {
                var timestampKandidatTidligereInnsending = firstNonNull(i.getInnsendt(), i.getOpprettet());
                var timestampInnsending = firstNonNull(h.getInnsendt(), h.getOpprettet());
                return timestampKandidatTidligereInnsending.isBefore(timestampInnsending);
            })
            .anyMatch(hi -> ((InnsendingInnslag) hi).getHendelse().erInitiell());
    }

    private static TidslinjeHendelse ettersending(InnsendingInnslag h,
                                                  List<ArkivDokument> dokumenter) {
        if (dokumenter == null || dokumenter.isEmpty()) {
            LOG.info("Fant ikke dokumenter tilknyttet journalpostId {} for ettersending, fortsetter uten dokumenter", h.getJournalpostId());
        }
        return EttersendingHendelse.builder()
                                   .aktørType(AktørType.BRUKER)
                                   .dokumenter(dokumenter)
                                   .opprettet(firstNonNull(h.getInnsendt(), h.getOpprettet()))
                                   .tidslinjeHendelseType(TidslinjeHendelseType.ETTERSENDING)
                                   .build();
    }

    private static LocalDateTime firstNonNull(LocalDateTime a, LocalDateTime b) {
        return a != null ? a : b;
    }

}
