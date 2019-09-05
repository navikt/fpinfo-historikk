package no.nav.foreldrepenger.historikk.tjenester.søknad;

import static no.nav.foreldrepenger.historikk.tjenester.Hendelse.UKJENT;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.Hendelse;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag;

public class SøknadsHistorikkInnslag extends HistorikkInnslag {

    private static final Logger LOG = LoggerFactory.getLogger(SøknadsHistorikkInnslag.class);
    private final Hendelse hendelse;
    private List<String> vedlegg;
    private LocalDate behandlingsdato;

    @JsonCreator
    public SøknadsHistorikkInnslag(@JsonProperty("fnr") Fødselsnummer fnr, @JsonProperty("hendelse") String hendelse) {
        super(fnr);
        this.hendelse = hendelseFra(hendelse);
    }

    private Hendelse hendelseFra(String hendelse) {
        return Optional.ofNullable(hendelse)
                .map(Hendelse::tilHendelse)
                .orElse(UKJENT);
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

    public Hendelse getHendelse() {
        return hendelse;
    }

    public LocalDateTime getOpprettet() {
        return opprettet;
    }

    public void setOpprettet(LocalDateTime opprettet) {
        this.opprettet = opprettet;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[hendelse=" + hendelse + ", fnr=" + getFnr() + ", aktørId=" + getAktørId()
                + ", journalpostId=" + getJournalpostId() + ", saksnr=" + getSaksnr() + ", opprettet=" + opprettet
                + ", vedlegg="
                + vedlegg + ", behandlingsdato=" + behandlingsdato + "]";
    }

}
