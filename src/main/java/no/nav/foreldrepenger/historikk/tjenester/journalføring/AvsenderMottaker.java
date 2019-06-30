package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import static no.nav.foreldrepenger.historikk.tjenester.journalføring.IdType.FNR;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public class AvsenderMottaker {

    private final Fødselsnummer id;
    private final IdType idType;
    private final String navn;

    public AvsenderMottaker(Fødselsnummer id, String navn) {
        this(id, FNR, navn);
    }

    public AvsenderMottaker(Fødselsnummer id, IdType idType, String navn) {
        this.id = id;
        this.idType = idType;
        this.navn = navn;
    }

    public Fødselsnummer getId() {
        return id;
    }

    public IdType getIdType() {
        return idType;
    }

    public String getNavn() {
        return navn;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + ", idType=" + idType + ", navn=" + navn + "]";
    }

}
