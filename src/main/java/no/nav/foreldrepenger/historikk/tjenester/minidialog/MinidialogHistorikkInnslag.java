package no.nav.foreldrepenger.historikk.tjenester.minidialog;

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

    @JsonCreator
    public MinidialogHistorikkInnslag(@JsonProperty("fnr") Fødselsnummer fnr,
            @JsonProperty("hendelse") String hendelse) {
        super(fnr);
        this.hendelse = hendelseFra(hendelse);
    }

    public Hendelse getHendelse() {
        return hendelse;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[hendelse=" + hendelse + ", fnr=" + getFnr() + ", aktørId=" + getAktørId()
                + ", journalpostId=" + getJournalpostId() + ", saksnr=" + getSaksnr() + ", opprettet=" + opprettet
                + "]";
    }
}
