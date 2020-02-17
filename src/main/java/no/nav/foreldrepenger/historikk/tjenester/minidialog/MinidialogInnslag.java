package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag;

public class MinidialogInnslag extends HistorikkInnslag {

    private final HendelseType hendelse;
    private final LocalDate gyldigTil;
    private final String tekst;
    private String dialogId;
    private Boolean aktiv;
    private final FagsakYtelseType ytelseType;

    @JsonCreator
    public MinidialogInnslag(
            @JsonProperty("hendelse") HendelseType hendelse, @JsonProperty("gyldigTil") LocalDate gyldigTil,
            @JsonProperty("tekst") String tekst, @JsonProperty("ytelseType") FagsakYtelseType ytelseType) {
        this.hendelse = hendelse;
        this.gyldigTil = gyldigTil;
        this.tekst = tekst;
        this.ytelseType = ytelseType;
    }

    public Boolean getAktiv() {
        return aktiv;
    }

    public FagsakYtelseType getYtelseType() {
        return ytelseType;
    }

    public String getDialogId() {
        return dialogId;
    }

    public void setDialogId(String dialogId) {
        this.dialogId = dialogId;
    }

    public String getTekst() {
        return tekst;
    }

    public LocalDate getGyldigTil() {
        return gyldigTil;
    }

    public HendelseType getHendelse() {
        return hendelse;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(Boolean aktiv) {
        this.aktiv = aktiv;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[hendelse=" + hendelse + ", gyldigTil=" + gyldigTil + ", tekst=" + tekst
                + ", dialogId=" + dialogId + ", aktiv=" + aktiv + ", ytelseType=" + ytelseType + ", opprettet="
                + opprettet + "]";
    }

}
