package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.github.resilience4j.retry.annotation.Retry;

@Service
@Retry(name = "dokarkiv")
public class JournalføringTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(JournalføringTjeneste.class);

    private final JournalføringConnection connection;

    public JournalføringTjeneste(JournalføringConnection connection) {
        this.connection = connection;
    }

    public String sluttfør(Journalpost journalpost) {
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
