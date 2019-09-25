package no.nav.foreldrepenger.historikk.tjenester.innsending;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag;

public class InnsendingInnslag extends HistorikkInnslag {

    private final HendelseType hendelse;
    private List<String> vedlegg;
    private LocalDate behandlingsdato;

    @JsonCreator
    public InnsendingInnslag(@JsonProperty("fnr") Fødselsnummer fnr, @JsonProperty("hendelse") HendelseType hendelse) {
        super(fnr);
        this.hendelse = hendelse;
    }

    public LocalDate getBehandlingsdato() {
        return behandlingsdato;
    }

    public void setBehandlingsdato(LocalDate behandlingsdato) {
        this.behandlingsdato = behandlingsdato;
    }

    public List<String> getVedlegg() {
        return vedlegg;
    }

    public void setVedlegg(List<String> vedlegg) {
        this.vedlegg = vedlegg;
    }

    public HendelseType getHendelse() {
        return hendelse;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[hendelse=" + hendelse + ", fnr=" + getFnr() + ", aktørId=" + getAktørId()
                + ", journalpostId=" + getJournalpostId() + ", saksnr=" + getSaksnr() + ", opprettet=" + opprettet
                + ", vedlegg=" + vedlegg + ", behandlingsdato=" + behandlingsdato + "]";
    }

}
