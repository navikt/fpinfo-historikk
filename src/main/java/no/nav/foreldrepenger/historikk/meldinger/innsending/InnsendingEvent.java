package no.nav.foreldrepenger.historikk.meldinger.innsending;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InnsendingEvent {

    private final String aktørId;
    private final String journalId;
    private final String referanseId;
    private final String saksNr;
    private final LeveranseStatus leveranseStatus;
    private final SøknadType type;
    private final String versjon;
    private final LocalDate gyldigTil;
    private List<String> vedlegg;

    @JsonCreator
    public InnsendingEvent(@JsonProperty("aktørId") String aktørId, @JsonProperty("journalId") String journalId,
            @JsonProperty("referanseId") String referanseId,
            @JsonProperty("saksNr") String saksNr, @JsonProperty("leveranseStatus") LeveranseStatus leveranseStatus,
            @JsonProperty("type") SøknadType type, @JsonProperty("versjon") String versjon,
            @JsonProperty("gyldigTil") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate gyldigTil,
            @JsonProperty("vedlegg") List<String> vedlegg) {
        this.aktørId = aktørId;
        this.journalId = journalId;
        this.referanseId = referanseId;
        this.saksNr = saksNr;
        this.leveranseStatus = leveranseStatus;
        this.type = type;
        this.versjon = versjon;
        this.gyldigTil = gyldigTil;
        this.vedlegg = vedlegg;
    }

    public LocalDate getGyldigTil() {
        return gyldigTil;
    }

    public String getAktørId() {
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

    public SøknadType getType() {
        return type;
    }

    public String getVersjon() {
        return versjon;
    }

    public List<String> getVedlegg() {
        return vedlegg;
    }

    @JsonIgnore
    public boolean erEttersending() {
        return getType().erEttersending();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[aktørId=" + aktørId + ", journalId=" + journalId + ", referanseId="
                + referanseId + ", saksNr=" + saksNr + ", leveranseStatus=" + leveranseStatus + ", type=" + type
                + ", versjon=" + versjon + ", gyldigTil=" + gyldigTil + ", vedlegg=" + vedlegg + "]";
    }

}
