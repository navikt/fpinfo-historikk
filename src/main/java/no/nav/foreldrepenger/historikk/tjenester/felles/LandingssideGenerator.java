package no.nav.foreldrepenger.historikk.tjenester.felles;

import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.isDev;

import java.net.URI;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class LandingssideGenerator implements EnvironmentAware {

    private Environment env;

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    public URI uri(HendelseType hendelse) {
        if (hendelse.erEngangsstønad()) {
            return URI.create(es());
        }
        if (hendelse.erForeldrepenger()) {
            return URI.create(fp());
        }
        if (hendelse.erSvangerskapspenger()) {
            return URI.create(svp());
        }
        return URI.create(fp());
    }

    private String fp() {
        return isDev(env) ? "https://foreldrepenger-q.nav.no/" : "https://foreldrepenger.nav.no";
    }

    private String svp() {
        return isDev(env) ? "https://foreldrepenger-q.nav.no/" : "https://foreldrepenger.nav.no";
    }

    private String es() {
        return isDev(env) ? "https://foreldrepenger-q.nav.no/" : "https://foreldrepenger.nav.no";
    }

}
