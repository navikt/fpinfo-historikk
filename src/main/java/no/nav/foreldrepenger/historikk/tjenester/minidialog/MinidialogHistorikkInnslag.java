package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.Hendelse;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag;

public class MinidialogHistorikkInnslag extends HistorikkInnslag {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogHistorikkInnslag.class);
    private final Hendelse hendelse;
    private final LocalDate gyldigTil;
    private final String journalpostId;

    @JsonCreator
    public MinidialogHistorikkInnslag(@JsonProperty("fnr") Fødselsnummer fnr,
            @JsonProperty("hendelse") String hendelse, @JsonProperty("gyldigTil") LocalDate gyldigTil,
            @JsonProperty("journalpostId") String journalpostId) {
        super(fnr);
        this.hendelse = hendelseFra(hendelse);
        this.gyldigTil = gyldigTil;
        this.journalpostId = journalpostId;
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public LocalDate getGyldigTil() {
        return gyldigTil;
    }

    public Hendelse getHendelse() {
        return hendelse;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[hendelse=" + hendelse + ", gyldigTil=" + gyldigTil + ", journalpostId="
                + journalpostId + ", saksnr=" + getSaksnr() + ", opprettet=" + getOpprettet()
                + ",aktørId=" + getAktørId() + ", fnr=" + getFnr() + "]";
    }
}
