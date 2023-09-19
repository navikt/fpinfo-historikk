package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import java.util.Arrays;
import java.util.Set;

public enum Brevkode {
    FORELDREPENGER_ANNULLERT("ANUFOR"),
    FORELDREPENGER_AVSLAG("AVSFOR"),
    SVANGERSKAPSPENGER_OPPHØR("OPPSVP"),
    ENGANGSSTØNAD_INNVILGELSE("INNVES"),
    SVANGERSKAPSPENGER_AVSLAG("AVSSVP"),
    FORELDREPENGER_INNVILGELSE("INVFOR"),
    ENGANGSSTØNAD_AVSLAG("AVSLES"),
    FORELDREPENGER_OPPHØR("OPPFOR"),
    SVANGERSKAPSPENGER_INNVILGELSE("INVSVP"),
    INNHENTE_OPPLYSNINGER("INNOPP"),
    ETTERLYS_INNTEKTSMELDING("ELYSIM"),
    FRITEKSTBREV("FRITEK"),

    // Gamle/utdaterte brevkoder funnet i Joark
    VEDTAK_POSITIVT_OLD("000048"),
    VEDTAK_AVSLAG_OLD("000051"),
    VEDTAK_FORELDREPENGER_OLD("000061"),
    VEDTAK_AVSLAG_FORELDREPENGER_OLD("000080"),
    INNHENTE_OPPLYSNINGER_OLD("000049"),
    ETTERLYS_INNTEKTSMELDING_OLD("000096"),

    // Gamle/utdaterte brevkoder funnet i Joark
    VEDTAK_POSITIVT_OLD_MF("MF_000048"),
    VEDTAK_AVSLAG_OLD_MF("MF_000051"),
    VEDTAK_FORELDREPENGER_OLD_MF("MF_000061"),
    VEDTAK_AVSLAG_FORELDREPENGER_OLD_MF("MF_000080"),
    INNHENTE_OPPLYSNINGER_OLD_MF("MF_000049"),
    ETTERLYS_INNTEKTSMELDING_OLD_MF("MF_000096"),

    UKJENT("UKJENT");

    private String kode;

    Brevkode(String kode) {
        this.kode = kode;
    }

    public String kode() {
        return kode;
    }

    public static Brevkode fraKode(String kode) {
        return Arrays.stream(values())
            .filter(brevkode -> brevkode.kode().equals(kode))
            .findFirst()
            .orElseThrow();
    }

    public boolean erVedtaksbrev() {
        return Set.of(
                FORELDREPENGER_ANNULLERT,
                FORELDREPENGER_AVSLAG,
                SVANGERSKAPSPENGER_OPPHØR,
                ENGANGSSTØNAD_INNVILGELSE,
                SVANGERSKAPSPENGER_AVSLAG,
                FORELDREPENGER_INNVILGELSE,
                ENGANGSSTØNAD_AVSLAG,
                FORELDREPENGER_OPPHØR,
                SVANGERSKAPSPENGER_INNVILGELSE,
                VEDTAK_POSITIVT_OLD,
                VEDTAK_POSITIVT_OLD_MF,
                VEDTAK_AVSLAG_OLD,
                VEDTAK_AVSLAG_OLD_MF,
                VEDTAK_FORELDREPENGER_OLD,
                VEDTAK_FORELDREPENGER_OLD_MF,
                VEDTAK_AVSLAG_FORELDREPENGER_OLD,
                VEDTAK_AVSLAG_FORELDREPENGER_OLD_MF
            )
            .contains(this);
    }

    public boolean erInnhentOpplysningerbrev() {
        return Set.of(INNHENTE_OPPLYSNINGER, INNHENTE_OPPLYSNINGER_OLD, INNHENTE_OPPLYSNINGER_OLD_MF).contains(this);
    }

    public boolean erEtterlysInntektsmeldingbrev() {
        return Set.of(ETTERLYS_INNTEKTSMELDING, ETTERLYS_INNTEKTSMELDING_OLD, ETTERLYS_INNTEKTSMELDING_OLD_MF).contains(this);
    }
}
