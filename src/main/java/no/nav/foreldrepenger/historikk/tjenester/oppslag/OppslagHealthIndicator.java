package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.historikk.health.EnvironmentAwareHealthIndicator;

@Component
public class OppslagHealthIndicator extends EnvironmentAwareHealthIndicator {
    public OppslagHealthIndicator(OppslagConnection connection) {
        super(connection);
    }
}
