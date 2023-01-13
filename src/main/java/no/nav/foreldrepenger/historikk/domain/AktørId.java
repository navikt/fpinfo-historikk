package no.nav.foreldrepenger.historikk.domain;

import static no.nav.foreldrepenger.common.domain.validation.InputValideringRegex.BARE_TALL;
import static no.nav.foreldrepenger.common.util.StringUtil.mask;

import java.util.Objects;

import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonValue;

@Embeddable
public class AktørId {

    @JsonValue
    @Pattern(regexp = BARE_TALL)
    private String aktørId;

    AktørId() {
    }

    public void setAktørId(String aktørId) {
        this.aktørId = aktørId;
    }

    public String getAktørId() {
        return aktørId;
    }

    public static AktørId valueOf(String aktørId) {
        AktørId id = new AktørId();
        id.setAktørId(aktørId);
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(aktørId);
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
        var other = (AktørId) obj;
        if (aktørId == null) {
            return other.aktørId == null;
        } return aktørId.equals(other.aktørId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [aktørId=" + mask(aktørId) + "]";
    }
}
