package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import java.util.Optional;

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

    public String journalfør(Journalpost journalpost, boolean sluttfør) {
        LOG.info("Oppretter journalpost {}", journalpost);
        return Optional.ofNullable(connection.opprettJournalpost(journalpost, sluttfør))
                .map(JournalføringRespons::getJournalpostId)
                .orElse(null);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[connection=" + connection + "]";
    }

}
