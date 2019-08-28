package no.nav.foreldrepenger.historikk.tjenester.sts;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.historikk.health.EnvironmentAwareHealthIndicator;

@Component
@ConditionalOnProperty(name = "sts.enabled", havingValue = "true")
public class STSHealthIndicator extends EnvironmentAwareHealthIndicator {
    public STSHealthIndicator(STSConnection connection) {
        super(connection);
    }
}
