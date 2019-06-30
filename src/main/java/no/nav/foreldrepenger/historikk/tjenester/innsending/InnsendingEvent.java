package no.nav.foreldrepenger.historikk.tjenester.innsending;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public class InnsendingEvent {

    private final AktørId aktørId;
    private final Fødselsnummer fnr;
    private final String journalId;
    private final String referanseId;
    private final String saksNr;
    private final LeveranseStatus leveranseStatus;
    private final Hendelse hendelse;
    private final LocalDate gyldigTil;
    private List<String> vedlegg;

    @JsonCreator
    public InnsendingEvent(@JsonProperty("aktørId") String aktørId,
            @JsonProperty("fnr") String fnr,
            @JsonProperty("journalId") String journalId,
            @JsonProperty("referanseId") String referanseId,
            @JsonProperty("saksNr") String saksNr,
            @JsonProperty("leveranseStatus") LeveranseStatus leveranseStatus,
            @JsonProperty("hendelse") Hendelse hendelse,
            @JsonProperty("gyldigTil") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate gyldigTil,
            @JsonProperty("vedlegg") List<String> vedlegg) {
        this.aktørId = AktørId.valueOf(aktørId);
        this.fnr = Fødselsnummer.valueOf(fnr);
        this.journalId = journalId;
        this.referanseId = referanseId;
        this.saksNr = saksNr;
        this.leveranseStatus = leveranseStatus;
        this.hendelse = hendelse;
        this.gyldigTil = gyldigTil;
        this.vedlegg = vedlegg;
    }

    public Fødselsnummer getFnr() {
        return fnr;
    }

    public void setVedlegg(List<String> vedlegg) {
        this.vedlegg = vedlegg;
    }

    public LocalDate getGyldigTil() {
        return gyldigTil;
    }

    public AktørId getAktørId() {
        return aktørId;
    }

    public String getJournalId() {
        return journalId;
    }

    public String getReferanseId() {
        return referanseId;
    }

    public String getSaksNr() {
        return saksNr;
    }

    public LeveranseStatus getLeveranseStatus() {
        return leveranseStatus;
    }

    public Hendelse getHendelse() {
        return hendelse;
    }

    public List<String> getVedlegg() {
        return vedlegg;
    }

    @JsonIgnore
    public boolean erEttersending() {
        return getHendelse().erEttersending();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[aktørId=" + aktørId + ", fnr=" + fnr + ", journalId=" + journalId
                + ", referanseId=" + referanseId + ", saksNr=" + saksNr + ", leveranseStatus=" + leveranseStatus
                + ", hendelse=" + hendelse + ", gyldigTil=" + gyldigTil + ", vedlegg=" + vedlegg + "]";
    }
}
