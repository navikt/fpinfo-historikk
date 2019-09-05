package no.nav.foreldrepenger.historikk.tjenester.søknad;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.Hendelse;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkHendelse;

public class SøknadInnsendingHendelse extends HistorikkHendelse {

    @NotNull
    private final SøknadsLeveranseStatus leveranseStatus;
    @Nullable
    private final List<String> vedlegg;
    @Nullable
    private final LocalDate førsteBehandlingsdato;
    @NotNull
    private final Hendelse hendelse;

    @JsonCreator
    public SøknadInnsendingHendelse(@JsonProperty("aktørId") AktørId aktørId,
            @JsonProperty("fnr") Fødselsnummer fnr,
            @JsonProperty("journalId") String journalId,
            @JsonProperty("referanseId") String referanseId,
            @JsonProperty("saksNr") String saksNr,
            @JsonProperty("leveranseStatus") SøknadsLeveranseStatus leveranseStatus,
            @JsonProperty("hendelse") @JsonAlias("type") Hendelse hendelse,
            @JsonProperty("vedlegg") List<String> vedlegg,
            @JsonProperty("førsteBehandlingsdato") LocalDate førsteBehandlingsdato) {
        super(aktørId, fnr, journalId, referanseId, saksNr);
        this.leveranseStatus = leveranseStatus;
        this.vedlegg = vedlegg;
        this.førsteBehandlingsdato = førsteBehandlingsdato;
        this.hendelse = hendelse;
    }

    public Hendelse getHendelse() {
        return hendelse;
    }

    public SøknadsLeveranseStatus getLeveranseStatus() {
        return leveranseStatus;
    }

    public List<String> getVedlegg() {
        return vedlegg;
    }

    @JsonIgnore
    public boolean erEttersending() {
        return hendelse.erEttersending();
    }

    public LocalDate getFørsteBehandlingsdato() {
        return førsteBehandlingsdato;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[leveranseStatus=" + leveranseStatus + ", vedlegg=" + vedlegg
                + ", førsteBehandlingsdato=" + førsteBehandlingsdato + ", getAktørId()=" + getAktørId() + ", fnr="
                + getFnr() + ", journalId=" + getJournalId() + ", referanseId=" + getReferanseId()
                + ", saksnr=" + getSaksNr() + ", hendelse=" + hendelse + "]";
    }

}
