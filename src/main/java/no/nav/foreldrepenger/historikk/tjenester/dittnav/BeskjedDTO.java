package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BeskjedDTO {
    private final String fnr;
    private final String grupperingsId;
    private final int sikkerhetsNivå;
    private final String link;
    private final String tekst;
    private final LocalDate synligFramTil;
    private final String eventId;

    @JsonCreator
    public BeskjedDTO(@JsonProperty("fnr") String fnr, @JsonProperty("grupperingsId") String grupperingsId,
            @JsonProperty("sikkerhetsNivå") int sikkerhetsNivå, @JsonProperty("link") String link,
            @JsonProperty("tekst") String tekst,
            @JsonProperty("synligFramTil") LocalDate synligFramTil,
            @JsonProperty("eventId") String eventId) {
        this.fnr = fnr;
        this.grupperingsId = grupperingsId;
        this.sikkerhetsNivå = sikkerhetsNivå;
        this.link = link;
        this.tekst = tekst;
        this.synligFramTil = synligFramTil;
        this.eventId = eventId;
    }

    public String getFnr() {
        return fnr;
    }

    public String getGrupperingsId() {
        return grupperingsId;
    }

    public int getSikkerhetsNivå() {
        return sikkerhetsNivå;
    }

    public String getLink() {
        return link;
    }

    public String getTekst() {
        return tekst;
    }

    public LocalDate getSynligFramTil() {
        return synligFramTil;
    }

    public String getEventId() {
        return eventId;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[fnr=" + fnr + ", grupperingsId=" + grupperingsId + ", sikkerhetsNivå="
                + sikkerhetsNivå + ", link=" + link + ", tekst=" + tekst + ", synligFramTil=" + synligFramTil
                + ", eventId=" + eventId + "]";
    }

}
