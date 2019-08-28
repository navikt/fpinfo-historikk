package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.historikk.health.EnvironmentAwareHealthIndicator;

@ConditionalOnProperty(name = "dokarkiv.enabled", havingValue = "true")
@Component
public class JournalføringHealthIndicator extends EnvironmentAwareHealthIndicator {
    public JournalføringHealthIndicator(JournalføringConnection connection) {
        super(connection);
    }
}
