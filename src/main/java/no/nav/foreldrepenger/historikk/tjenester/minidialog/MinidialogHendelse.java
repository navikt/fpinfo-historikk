package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonCreator;

import io.swagger.annotations.ApiModelProperty;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.Hendelse;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;

public class MinidialogHendelse extends Hendelse {

    @ApiModelProperty(example = "2999-12-12")
    @DateTimeFormat(iso = DATE)
    private final LocalDate gyldigTil;

    private final String tekst;

    @JsonCreator
    public MinidialogHendelse(AktørId aktørId, Fødselsnummer fnr, String referanseId, String saksNr,
            HendelseType hendelseType, String tekst, LocalDate gyldigTil) {
        super(aktørId, fnr, null, referanseId, saksNr, hendelseType);
        this.tekst = tekst;
        this.gyldigTil = gyldigTil;
    }

    public String getTekst() {
        return tekst;
    }

    public LocalDate getGyldigTil() {
        return gyldigTil;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[, gyldigTil=" + gyldigTil + ", tekst=" + tekst + ", hendelseType="
                + getHendelseType() + ", aktørId=" + getAktørId() + ", fnr=" + getFnr() + ", referanseId="
                + getReferanseId() + ", saksNr=" + getSaksNr() + "]";
    }

}
