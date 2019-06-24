package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.historikk.health.EnvironmentAwareHealthIndicator;

@Component
public class JournalføringHealthIndicator extends EnvironmentAwareHealthIndicator {
    public JournalføringHealthIndicator(JournalføringConnection connection) {
        super(connection);
    }
}
