package no.nav.foreldrepenger.historikk.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonValue;

@Embeddable
public class AktørId {

    @JsonValue
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
        AktørId other = (AktørId) obj;
        if (aktørId == null) {
            if (other.aktørId != null) {
                return false;
            }
        } else if (!aktørId.equals(other.aktørId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [aktørId=" + aktørId + "]";
    }
}