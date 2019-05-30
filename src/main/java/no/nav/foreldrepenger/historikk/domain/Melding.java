package no.nav.foreldrepenger.historikk.domain;

import java.time.LocalDate;

public class Melding {

    private final AktørId aktørId;
    private final String melding;
    private LocalDate dato;

    public Melding(AktørId aktørId, String melding) {
        this.aktørId = aktørId;
        this.melding = melding;
    }

    public LocalDate getDato() {
        return dato;
    }

    public void setDato(LocalDate dato) {
        this.dato = dato;
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
