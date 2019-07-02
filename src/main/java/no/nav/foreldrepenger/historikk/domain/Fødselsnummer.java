package no.nav.foreldrepenger.historikk.domain;

import static no.nav.foreldrepenger.historikk.util.StringUtil.mask;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Objects;

@Embeddable
public class Fødselsnummer {

    private String fnr;

    public static Fødselsnummer valueOf(String fnr) {
        Fødselsnummer id = new Fødselsnummer();
        id.setFnr(fnr);
        return id;
    }

    public void setFnr(String fnr) {
        this.fnr = fnr;
    }

    @JsonValue
    public String getFnr() {
        return fnr;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Fødselsnummer other = (Fødselsnummer) obj;
        if (fnr == null) {
            if (other.fnr != null) {
                return false;
            }
        } else if (!fnr.equals(other.fnr)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fnr);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [fnr=" + mask(fnr) + "]";
    }
}
