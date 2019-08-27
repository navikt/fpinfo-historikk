package no.nav.foreldrepenger.historikk.tjenester.historikk;

import static no.nav.foreldrepenger.historikk.tjenester.innsending.Hendelse.UKJENT;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.innsending.Hendelse;

class HistorikkInnslag {

    private static final Logger LOG = LoggerFactory.getLogger(HistorikkInnslag.class);
    private final Hendelse hendelse;
    private final Fødselsnummer fnr;
    private AktørId aktørId;
    private String journalpostId;
    private String saksnr;
    private LocalDateTime opprettet;

    @JsonCreator
    public HistorikkInnslag(@JsonProperty("fnr") Fødselsnummer fnr, @JsonProperty("hendelse") String hendelse) {
        this.fnr = fnr;
        this.hendelse = hendelseFra(hendelse);
    }

    private Hendelse hendelseFra(String hendelse) {
        return Optional.ofNullable(hendelse)
                .map(HistorikkInnslag::tilHendelse)
                .orElse(UKJENT);
    }

    private static Hendelse tilHendelse(String hendelse) {
        try {
            return Hendelse.valueOf(hendelse);
        } catch (Exception e) {
            LOG.warn("Kunne ikke utlede hendelse fra {}", hendelse);
            return UKJENT;
        }
    }

    public void setAktørId(AktørId aktørId) {
        this.aktørId = aktørId;
    }

    public AktørId getAktørId() {
        return aktørId;
    }

    public Fødselsnummer getFnr() {
        return fnr;
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public void setJournalpostId(String journalpostId) {
        this.journalpostId = journalpostId;
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

    public String getSaksnr() {
        return saksnr;
    }

    public void setSaksnr(String saksnr) {
        this.saksnr = saksnr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[aktørId=" + aktørId + ", fnr=" + fnr + ", journalpostId="
                + journalpostId + ", saksnr=" + saksnr + ", hendelse=" + hendelse + ", opprettet=" + opprettet + "]";
    }

}
