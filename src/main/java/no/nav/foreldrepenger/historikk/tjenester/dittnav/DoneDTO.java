package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DoneDTO {

    private final String fnr;
    private final String grupperingsId;
    private final String eventId;

    @JsonCreator
    public DoneDTO(@JsonProperty("fnr") String fnr, @JsonProperty("grupperingsId") String grupperingsId,
            @JsonProperty("eventId") String eventId) {
        this.fnr = fnr;
        this.grupperingsId = grupperingsId;
        this.eventId = eventId;
    }

    public String getFnr() {
        return fnr;
    }

    public String getGrupperingsId() {
        return grupperingsId;
    }

    public String getEventId() {
        return eventId;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[fnr=" + fnr + ", grupperingsId=" + grupperingsId + ", eventId="
                + eventId + "]";
    }

}
