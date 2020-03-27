package no.nav.foreldrepenger.historikk.tjenester.felles;

import static no.nav.foreldrepenger.boot.conditionals.EnvUtil.isDev;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class UrlGenerator implements EnvironmentAware {

    private Environment env;

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    public String url(HendelseType hendelse) {
        if (hendelse.erEngangsstønad()) {
            return es();
        }
        if (hendelse.erForeldrepenger()) {
            return fp();
        }
        if (hendelse.erSvangerskapspenger()) {
            return svp();
        }
        return fp();
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
