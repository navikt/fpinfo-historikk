package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "dokarkiv.enabled", havingValue = "true")
public class JoarkJournalføringTjeneste implements Journalføring {

    private static final Logger LOG = LoggerFactory.getLogger(JoarkJournalføringTjeneste.class);

    private final JournalføringConnection connection;

    public JoarkJournalføringTjeneste(JournalføringConnection connection) {
        this.connection = connection;
    }

    @Override
    public String journalfør(Journalpost journalpost) {
        LOG.info("Oppretter journalpost {}", journalpost);
        String id = connection.opprettJournalpost(journalpost, true);
        LOG.info("Opprettet journalpost med id {} OK", id);
        return id;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[connection=" + connection + "]";
    }

}
