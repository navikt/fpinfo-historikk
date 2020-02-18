package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.tjenester.felles.Hendelse;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;
import no.nav.foreldrepenger.historikk.tjenester.felles.YtelseType;

public class MinidialogHendelse extends Hendelse {

    @ApiModelProperty(example = "2999-12-12")
    @DateTimeFormat(iso = DATE)
    private final LocalDate gyldigTil;
    private final String dialogId;
    private final boolean aktiv;
    private final YtelseType ytelseType;

    @JsonCreator
    public MinidialogHendelse(@JsonProperty("aktørId") AktørId aktørId, @JsonProperty("dialogId") String dialogId,
            @JsonProperty("journalpostId") String journalpostId,
            @JsonProperty("dokumentId") String dokumentId,
            @JsonProperty("saksnummer") @JsonAlias("saksnr") String saksnummer,
            @JsonProperty("hendelse") HendelseType hendelse,
            @JsonProperty("gyldigTil") LocalDate gyldigTil,
            @JsonProperty("opprettet") LocalDateTime opprettet,
            @JsonProperty("aktiv") boolean aktiv,
            @JsonProperty("ytelseType") YtelseType ytelseType) {
        super(aktørId, journalpostId, dokumentId, saksnummer, hendelse, opprettet);
        this.gyldigTil = gyldigTil;
        this.dialogId = dialogId;
        this.aktiv = aktiv;
        this.ytelseType = ytelseType;
    }

    public YtelseType getYtelseType() {
        return ytelseType;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public String getDialogId() {
        return dialogId;
    }

    public LocalDate getGyldigTil() {
        return gyldigTil;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[gyldigTil=" + gyldigTil + ", dialogId=" + dialogId
                + ", aktiv=" + aktiv + ", ytelseType=" + ytelseType + ", opprettet=" + getOpprettet()
                + ", hendelse=" + getHendelse() + ", aktørId=" + getAktørId() + ", journalpostId="
                + getJournalpostId() + ", saksnummer=" + getSaksnummer() + "]";
    }

}
