package no.nav.foreldrepenger.historikk.health;

import static org.springframework.boot.actuate.health.Health.down;
import static org.springframework.boot.actuate.health.Health.up;

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
            return up()
                    .withDetail(pingable.name(), pingable.pingEndpoint() + " (Disabled)")
                    .build();
        }
        try {
            pingable.ping();
            return up()
                    .withDetail(pingable.name(), pingable.pingEndpoint())
                    .build();
        } catch (Exception e) {
            return down()
                    .withDetail(pingable.name(), pingable.pingEndpoint())
                    .withException(e)
                    .build();
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingable=" + pingable + "]";
    }
}
