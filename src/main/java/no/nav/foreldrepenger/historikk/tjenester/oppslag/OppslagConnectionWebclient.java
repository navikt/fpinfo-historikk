package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import no.nav.boot.conditionals.ConditionalOnNotProd;
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
@ConditionalOnNotProd
public class OppslagConnectionWebclient implements OppslagConnection, WebClientRetryAware {

    private static final Logger LOG = LoggerFactory.getLogger(OppslagConnectionWebclient.class);

    public static final String OPPSLAG = "OPPSLAG";
    private final WebClient client;
    private final OppslagConfig cfg;

    public OppslagConnectionWebclient(@Qualifier(OPPSLAG) WebClient client,
                                      OppslagConfig oppslagConfig) {
        this.client = client;
        this.cfg = oppslagConfig;
    }

    @Override
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

    @Override
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
