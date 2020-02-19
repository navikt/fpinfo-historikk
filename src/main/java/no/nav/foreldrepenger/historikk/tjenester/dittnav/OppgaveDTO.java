package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OppgaveDTO {

    private final String fnr;
    private final String grupperingsId;
    private final int sikkerhetsNivå;
    private final String link;
    private final String tekst;
    private final String eventId;

    @JsonCreator
    public OppgaveDTO(@JsonProperty("fnr") String fnr, @JsonProperty("grupperingsId") String grupperingsId,
            @JsonProperty("sikkerhetsNivå") int sikkerhetsNivå, @JsonProperty("link") String link,
            @JsonProperty("tekst") String tekst, @JsonProperty("eventId") String eventId) {
        this.fnr = fnr;
        this.grupperingsId = grupperingsId;
        this.sikkerhetsNivå = sikkerhetsNivå;
        this.link = link;
        this.tekst = tekst;
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

    public String getEventId() {
        return eventId;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[fnr=" + fnr + ", grupperingsId=" + grupperingsId + ", sikkerhetsNivå="
                + sikkerhetsNivå + ", eventId=" + eventId + ", link=" + link + ", tekst=" + tekst + "]";
    }

}
