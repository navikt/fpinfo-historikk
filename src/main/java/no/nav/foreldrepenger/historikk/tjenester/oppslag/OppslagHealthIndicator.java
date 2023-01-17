package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import no.nav.boot.conditionals.ConditionalOnProd;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.historikk.health.AbstractPingableHealthIndicator;

@Component
@ConditionalOnProd
public class OppslagHealthIndicator extends AbstractPingableHealthIndicator {
    public OppslagHealthIndicator(OppslagConnectionRestTemplate connection) {
        super(connection);
    }
}
