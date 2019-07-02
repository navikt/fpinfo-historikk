package no.nav.foreldrepenger.historikk.tjenester.innsending;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public class SøknadInnsendingHendelse extends InnsendingHendelse {

    @NotNull
    private final LeveranseStatus leveranseStatus;
    @Nullable
    private final List<String> vedlegg;

    @JsonCreator
    public SøknadInnsendingHendelse(@JsonProperty("aktørId") AktørId aktørId,
            @JsonProperty("fnr") Fødselsnummer fnr,
            @JsonProperty("journalId") String journalId,
            @JsonProperty("referanseId") String referanseId,
            @JsonProperty("saksNr") String saksNr,
            @JsonProperty("leveranseStatus") LeveranseStatus leveranseStatus,
            @JsonProperty("hendelse") Hendelse hendelse,
            @JsonProperty("vedlegg") List<String> vedlegg) {
        super(aktørId, fnr, journalId, referanseId, saksNr, hendelse);
        this.leveranseStatus = leveranseStatus;
        this.vedlegg = vedlegg;
    }

    public LeveranseStatus getLeveranseStatus() {
        return leveranseStatus;
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
        return getClass().getSimpleName() + "[aktørId=" + getAktørId() + ", fnr=" + getFnr() + ", journalId="
                + getJournalId() + ", referanseId=" + getReferanseId() + ", saksNr=" + getSaksNr()
                + ", leveranseStatus=" + leveranseStatus + ", hendelse=" + getHendelse()
                + ", vedlegg=" + vedlegg + "]";
    }
}
