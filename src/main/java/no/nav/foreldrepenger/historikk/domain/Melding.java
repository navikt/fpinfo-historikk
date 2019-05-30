package no.nav.foreldrepenger.historikk.domain;

public class Melding {

    private final AktørId aktørId;
    private final String melding;

    public Melding(AktørId aktørId, String melding) {
        this.aktørId = aktørId;
        this.melding = melding;
    }

    public AktørId getAktørId() {
        return aktørId;
    }

    public String getMelding() {
        return melding;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [aktørId=" + aktørId + ", melding=" + melding + "]";
    }
}
