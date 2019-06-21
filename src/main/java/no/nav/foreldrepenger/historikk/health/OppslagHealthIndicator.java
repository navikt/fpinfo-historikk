package no.nav.foreldrepenger.historikk.health;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.historikk.tjenester.oppslag.OppslagConnection;

@Component
public class OppslagHealthIndicator extends EnvironmentAwareHealthIndicator {
    public OppslagHealthIndicator(OppslagConnection connection) {
        super(connection);
    }
}
