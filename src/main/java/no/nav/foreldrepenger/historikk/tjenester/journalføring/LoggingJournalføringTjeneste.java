package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import io.github.resilience4j.retry.annotation.Retry;

@Service
@Retry(name = "dokarkiv")
@ConditionalOnProperty(name = "dokarkiv.enabled", havingValue = "false")
public class LoggingJournalføringTjeneste implements Journalføring {
    private static final String ID = "42";
    private static final Logger LOG = LoggerFactory.getLogger(LoggingJournalføringTjeneste.class);

    @Override
    public String journalfør(Journalpost journalpost) {
        LOG.info("Opprettet journalpost {}  med id {} OK liksom", ID);
        return ID;
    }
}
