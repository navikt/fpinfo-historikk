package no.nav.foreldrepenger.historikk.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import no.nav.foreldrepenger.historikk.http.PingEndpointAware;

public abstract class AbstractPingableHealthIndicator implements HealthIndicator {
    private final PingEndpointAware pingable;

    public AbstractPingableHealthIndicator(PingEndpointAware pingable) {
        this.pingable = pingable;
    }

    @Override
    public Health health() {
        if (!pingable.isEnabled()) {
            return Health.up().withDetail(pingable.name(), pingable.pingEndpoint() + " (Disabled)").build();
        }
        try {
            pingable.ping();
            return up();
        } catch (Exception e) {
            return down(e);
        }
    }

    private Health up() {
        return Health.up()
                .withDetail(pingable.name(), pingable.pingEndpoint())
                .build();
    }

    private Health down(Exception e) {
        return Health.down()
                .withDetail(pingable.name(), pingable.pingEndpoint())
                .withException(e)
                .build();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingable=" + pingable + "]";
    }
}
