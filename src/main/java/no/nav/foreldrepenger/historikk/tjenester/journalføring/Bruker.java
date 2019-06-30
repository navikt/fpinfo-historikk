package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import static no.nav.foreldrepenger.historikk.tjenester.journalføring.BrukerIdType.FNR;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public class Bruker {
    private final BrukerIdType idType;
    private final Fødselsnummer id;

    public Bruker(Fødselsnummer id) {
        this(FNR, id);
    }

    private Bruker(BrukerIdType idType, Fødselsnummer id) {
        this.idType = idType;
        this.id = id;
    }

    public BrukerIdType getIdType() {
        return idType;
    }

    public Fødselsnummer getId() {
        return id;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[idType=" + idType + ", id=" + id + "]";
    }
}
