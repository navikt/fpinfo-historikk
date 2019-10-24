package no.nav.foreldrepenger.historikk.tjenester.aktør;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ident {
    private final String ident;
    private final IdentGruppe identGruppe;
    private final boolean gjeldende;

    @JsonCreator
    public Ident(@JsonProperty("ident") String ident, @JsonProperty("identGruppe") IdentGruppe identGruppe,
            @JsonProperty("gjeldende") boolean gjeldende) {
        this.ident = ident;
        this.identGruppe = identGruppe;
        this.gjeldende = gjeldende;
    }

    public String getIdent() {
        return ident;
    }

    public IdentGruppe getIdentGruppe() {
        return identGruppe;
    }

    public boolean isGjeldende() {
        return gjeldende;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[ident=" + ident + ", identGruppe=" + identGruppe + ", gjeldende="
                + gjeldende + "]";
    }

}
