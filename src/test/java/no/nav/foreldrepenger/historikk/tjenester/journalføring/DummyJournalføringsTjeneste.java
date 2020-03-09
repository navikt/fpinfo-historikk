package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.tjenester.dokarkiv.JoarkJournalføringTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.dokarkiv.Journalpost;

@Primary
@Service
public class DummyJournalføringsTjeneste extends JoarkJournalføringTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(DummyJournalføringsTjeneste.class);

    public DummyJournalføringsTjeneste() {
        super(null);
    }

    @Override
    public String journalfør(Journalpost journalpost) {
        LOG.info("Sluttfører journalpost {}", journalpost);
        return "42";
    }
}