package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;
import no.nav.foreldrepenger.historikk.tjenester.felles.YtelseType;
import no.nav.foreldrepenger.historikk.util.EnvUtil;

@Component
public class DittNavUrlGenerator implements EnvironmentAware {

    private Environment env;

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    public String url(YtelseType ytelseType) {
        switch (ytelseType) {
        case ES:
            return es();
        case SVP:
            return svp();
        case FP:
        default:
            return fp();
        }
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
        return EnvUtil.isDev(env) ? "https://foreldrepengesoknad-q.nav.no/" : "https://foreldrepengesoknad.nav.no";
    }

    private String svp() {
        return EnvUtil.isDev(env) ? "https://svangerskapspenger-q.nav.no/" : "https://svangerskapspenger.nav.no";
    }

    private String es() {
        return EnvUtil.isDev(env) ? "https://engangsstonad-q.nav.no/" : "https://engangsstonad.nav.no";
    }

}
