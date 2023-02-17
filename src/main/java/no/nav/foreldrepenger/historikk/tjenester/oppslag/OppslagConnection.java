package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.http.WebClientRetryAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static no.nav.boot.conditionals.EnvUtil.CONFIDENTIAL;
import static no.nav.foreldrepenger.common.util.StringUtil.taint;

@Component
public class OppslagConnection implements WebClientRetryAware {

    private static final Logger LOG = LoggerFactory.getLogger(OppslagConnection.class);

    public static final String OPPSLAG = "OPPSLAG";
    private final WebClient client;
    private final OppslagConfig cfg;

    public OppslagConnection(@Qualifier(OPPSLAG) WebClient client,
                             OppslagConfig oppslagConfig) {
        this.client = client;
        this.cfg = oppslagConfig;
    }

    public AktørId hentAktørId() {
        return client.get()
            .uri(cfg.aktørPath())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(AktørId.class)
            .doOnSuccess(s -> LOG.trace(CONFIDENTIAL, "Fikk aktørId {} fra mottak", s.getAktørId()))
            .doOnError(s -> LOG.info("Feil i oppslag mot mottak"))
            .block();
    }

    public String orgNavn(String orgnr) {
        return client.get()
            .uri(cfg.orgnavnPathTemplate(), taint(orgnr))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(String.class)
            .doOnSuccess(s -> LOG.trace(CONFIDENTIAL, "Fikk orgnavn {} fra mottak for orgnummer {}", s, taint(orgnr)))
            .doOnError(s -> LOG.info("Feil i oppslag mot mottak"))
            .block();
    }

}
