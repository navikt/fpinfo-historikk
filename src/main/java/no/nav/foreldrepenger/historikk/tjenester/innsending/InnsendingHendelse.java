package no.nav.foreldrepenger.historikk.tjenester.innsending;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.tjenester.felles.Hendelse;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;

public class InnsendingHendelse extends Hendelse {

    @NotNull
    private final InnsendingLeveranseStatus leveranseStatus;
    @Nullable
    private final List<String> ikkeOpplastedeVedlegg;
    private final List<String> opplastedeVedlegg;

    @Nullable
    private final LocalDate førsteBehandlingsdato;
    private final String referanseId;
    private final String dialogId;

    @JsonCreator
    public InnsendingHendelse(@JsonProperty("aktørId") AktørId aktørId,
            @JsonProperty("journalId") String journalId,
            @JsonProperty("referanseId") String referanseId,
            @JsonProperty("dialogId") String dialogId,
            @JsonProperty("saksNr") String saksNr,
            @JsonProperty("leveranseStatus") InnsendingLeveranseStatus leveranseStatus,
            @JsonProperty("hendelseType") @JsonAlias("hendelse") HendelseType hendelseType,
            @JsonProperty("opplastedeVedlegg") List<String> opplastedeVedlegg,
            @JsonProperty("ikkeOpplastedeVedlegg") List<String> ikkeOpplastedeVedlegg,

            @JsonProperty("førsteBehandlingsdato") LocalDate førsteBehandlingsdato) {
        super(aktørId, journalId, saksNr, hendelseType);
        this.leveranseStatus = leveranseStatus;
        this.dialogId = dialogId;
        this.referanseId = referanseId;
        this.opplastedeVedlegg = opplastedeVedlegg;
        this.ikkeOpplastedeVedlegg = ikkeOpplastedeVedlegg;
        this.førsteBehandlingsdato = førsteBehandlingsdato;
    }

    public String getDialogId() {
        return dialogId;
    }

    public InnsendingLeveranseStatus getLeveranseStatus() {
        return leveranseStatus;
    }

    public String getReferanseId() {
        return referanseId;
    }

    public LocalDate getFørsteBehandlingsdato() {
        return førsteBehandlingsdato;
    }

    public List<String> getIkkeOpplastedeVedlegg() {
        return ikkeOpplastedeVedlegg;
    }

    public List<String> getOpplastedeVedlegg() {
        return opplastedeVedlegg;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[leveranseStatus=" + leveranseStatus + ", ikkeOpplastedeVedlegg="
                + ikkeOpplastedeVedlegg + ", opplastedeVedlegg=" + opplastedeVedlegg + ", førsteBehandlingsdato="
                + førsteBehandlingsdato + ", referanseId=" + referanseId + ", dialogId=" + dialogId + "]";
    }

}
