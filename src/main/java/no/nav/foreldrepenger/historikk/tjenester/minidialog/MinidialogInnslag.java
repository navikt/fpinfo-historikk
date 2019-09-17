package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.Hendelse;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag;

public class MinidialogInnslag extends HistorikkInnslag {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogInnslag.class);
    private final Hendelse hendelse;
    private final LocalDate gyldigTil;

    @JsonCreator
    public MinidialogInnslag(@JsonProperty("fnr") Fødselsnummer fnr,
            @JsonProperty("hendelse") String hendelse, @JsonProperty("gyldigTil") LocalDate gyldigTil,
            @JsonProperty("journalpostId") String journalpostId) {
        super(fnr);
        this.hendelse = hendelseFra(hendelse);
        this.gyldigTil = gyldigTil;
        super.setJournalpostId(journalpostId);
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
                + getJournalpostId() + ", saksnr=" + getSaksnr() + ", opprettet=" + getOpprettet()
                + ",aktørId=" + getAktørId() + ", fnr=" + getFnr() + "]";
    }
}
