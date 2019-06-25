package no.nav.foreldrepenger.historikk.tjenester.journalføring;

public class Bruker {
    private final BrukerIdType idType;
    private final String id;

    public Bruker(String id) {
        this(id.length() == 11 ? BrukerIdType.FNR : BrukerIdType.ORGNR, id);
    }

    private Bruker(BrukerIdType idType, String id) {
        this.idType = idType;
        this.id = id;
    }

    public BrukerIdType getIdType() {
        return idType;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[idType=" + idType + ", id=" + id + "]";
    }
}
