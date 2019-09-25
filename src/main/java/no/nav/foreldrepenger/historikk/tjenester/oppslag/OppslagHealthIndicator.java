package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.historikk.health.AbstractPingableHealthIndicator;

@Component
public class OppslagHealthIndicator extends AbstractPingableHealthIndicator {
    public OppslagHealthIndicator(OppslagConnection connection) {
        super(connection);
    }
}
