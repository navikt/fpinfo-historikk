package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Svar {
    private final String id;
    private final String saksnr;
    private final String svar;

    @JsonCreator
    public Svar(@JsonProperty("id") String id, @JsonProperty("saksnr") String saksnr,
            @JsonProperty("svar") String svar) {
        this.id = id;
        this.saksnr = saksnr;
        this.svar = svar;
    }

    public String getId() {
        return id;
    }

    public String getSaksnr() {
        return saksnr;
    }

    public String getSvar() {
        return svar;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[id=" + id + ", saksnr=" + saksnr + ", svar=" + svar + "]";
    }

}
