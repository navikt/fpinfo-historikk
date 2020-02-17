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
    private final String dialogId;
    private final String tekst;
    private final boolean aktiv;
    private final FagsakYtelseType ytelseType;

    @JsonCreator
    public MinidialogHendelse(AktørId aktørId, String dialogId, String journalId, String dokumentId,
            String saksnummer, HendelseType hendelse, String tekst, LocalDate gyldigTil, LocalDateTime innsendt,
            boolean aktiv, FagsakYtelseType ytelseType) {
        super(aktørId, journalId, dokumentId, saksnummer, hendelse, innsendt);
        this.tekst = tekst;
        this.gyldigTil = gyldigTil;
        this.dialogId = dialogId;
        this.aktiv = aktiv;
        this.ytelseType = ytelseType;
    }

    public FagsakYtelseType getYtelseType() {
        return ytelseType;
    }

    public boolean isAktiv() {
        return aktiv;
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
        return getClass().getSimpleName() + "[gyldigTil=" + gyldigTil + ", dialogId=" + dialogId + ", tekst=" + tekst
                + ", aktiv=" + aktiv + ", ytelseType=" + ytelseType + ", innsendt=" + getInnsendt()
                + ", hendelse=" + getHendelse() + ", getAktørId()=" + getAktørId() + ", journalId="
                + getJournalId() + ", saksnummer=" + getSaksnummer() + "]";
    }

}
