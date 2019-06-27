package no.nav.foreldrepenger.historikk.health;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.isDevOrPreprod;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import no.nav.foreldrepenger.historikk.http.PingEndpointAware;

public abstract class EnvironmentAwareHealthIndicator implements HealthIndicator, EnvironmentAware {
    private final PingEndpointAware pingable;
    private Environment env;

    public EnvironmentAwareHealthIndicator(PingEndpointAware pingable) {
        this.pingable = pingable;
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
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
        return isDevOrPreprod(env) ? Health.up()
                .withDetail(pingable.name(), pingable.pingEndpoint())
                .build()
                : Health.up().build();
    }

    private Health down(Exception e) {
        return isDevOrPreprod(env)
                ? Health.down()
                        .withDetail(pingable.name(), pingable.pingEndpoint())
                        .withException(e)
                        .build()
                : Health.down().build();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [pingable=" + pingable + "isDevOrPreprod " + isDevOrPreprod(env) + "]";
    }
}
