package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.http.AbstractRestConnection;

@Component
public class OppslagConnection extends AbstractRestConnection {
    public static final Logger LOG = LoggerFactory.getLogger(OppslagConnection.class);
    private final OppslagConfig cfg;

    public OppslagConnection(RestOperations restOperations, OppslagConfig config) {
        super(restOperations, config);
        this.cfg = config;
    }

    @Override
    public URI pingEndpoint() {
        return cfg.pingURI();
    }

    public AktørId hentAktørId() {
        return getForObject(cfg.aktørURI(), AktørId.class, true);
    }

    @Cacheable(cacheNames = "fnr")
    public Fødselsnummer hentFnr(AktørId aktørId) {
        return getForObject(cfg.fnrURI(aktørId.getAktørId()), Fødselsnummer.class, true);
    }

    @Cacheable(cacheNames = "aktør")
    public String hentNavn(AktørId aktørId) {
        return hentNavn(hentFnr(aktørId));
    }

    @Cacheable(cacheNames = "fnr")
    private String hentNavn(Fødselsnummer fnr) {
        return getForObject(cfg.personNavnURI(fnr), String.class);
    }

    @Cacheable(cacheNames = "organisaasjon")
    String orgNavn(String orgnr) {
        return getForObject(cfg.orgNavnURI(orgnr), String.class);
    }

    @Override
    public boolean isEnabled() {
        return cfg.isEnabled();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [cfg=" + cfg + "]";
    }

}
