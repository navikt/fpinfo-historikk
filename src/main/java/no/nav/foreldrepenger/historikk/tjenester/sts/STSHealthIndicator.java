package no.nav.foreldrepenger.historikk.tjenester.sts;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.historikk.health.EnvironmentAwareHealthIndicator;

@Component
public class STSHealthIndicator extends EnvironmentAwareHealthIndicator {
    public STSHealthIndicator(STSConnection connection) {
        super(connection);
    }
}
