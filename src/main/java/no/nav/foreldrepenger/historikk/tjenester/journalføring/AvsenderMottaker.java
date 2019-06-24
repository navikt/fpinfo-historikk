package no.nav.foreldrepenger.historikk.tjenester.journalføring;

public class AvsenderMottaker {

    private final String id;
    private final IdType idType;
    private final String navn;

    public AvsenderMottaker(String id, IdType idType, String navn) {
        this.id = id;
        this.idType = idType;
        this.navn = navn;
    }

    public String getId() {
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
