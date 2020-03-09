package no.nav.foreldrepenger.historikk.tjenester.sts;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.historikk.health.AbstractPingableHealthIndicator;

@Component
@ConditionalOnProperty(name = "historikk.sts.enabled")
public class STSHealthIndicator extends AbstractPingableHealthIndicator {
    public STSHealthIndicator(STSConnection connection) {
        super(connection);
    }
}
