package no.nav.foreldrepenger.historikk.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModelProperty;

@Embeddable
public class Versjon {
    @ApiModelProperty(example = "1.0")
    private String versjon;

    public static Versjon valueOf(String versjon) {
        Versjon v = new Versjon();
        v.setVersjon(versjon);
        return v;
    }

    public void setVersjon(String v) {
        this.versjon = v;
    }

    @JsonValue
    public String getVersjon() {
        return versjon;
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
        Versjon other = (Versjon) obj;
        if (versjon == null) {
            if (other.versjon != null) {
                return false;
            }
        } else if (!versjon.equals(other.versjon)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(versjon);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [versjon=" + versjon + "]";
    }
}
