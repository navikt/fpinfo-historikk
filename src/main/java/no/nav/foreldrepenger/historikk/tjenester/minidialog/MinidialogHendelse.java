package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonCreator;

import io.swagger.annotations.ApiModelProperty;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.tjenester.felles.Hendelse;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;

public class MinidialogHendelse extends Hendelse {

    @ApiModelProperty(example = "2999-12-12")
    @DateTimeFormat(iso = DATE)
    private final LocalDate gyldigTil;
    private final LocalDateTime innsendingsTidspunkt;

    private final String dialogId;
    private final String tekst;

    @JsonCreator
    public MinidialogHendelse(AktørId aktørId, String dialogId, String saksNr,
            HendelseType hendelseType, String tekst, LocalDate gyldigTil, LocalDateTime innsendingsTidspunkt) {
        super(aktørId, null, saksNr, hendelseType);
        this.tekst = tekst;
        this.gyldigTil = gyldigTil;
        this.dialogId = dialogId;
        this.innsendingsTidspunkt = innsendingsTidspunkt;
    }

    public LocalDateTime getInnsendingsTidspunkt() {
        return innsendingsTidspunkt;
    }

    public String getDialogId() {
        return dialogId;
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
                + getHendelse() + ", aktørId=" + getAktørId() + ", dialogId="
                + getDialogId() + ", saksNr=" + getSaksnummer() + ", innsendingstidspunkt=" + innsendingsTidspunkt
                + "]";
    }

}
