package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.http.AbstractRestConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return cfg.pingEndpoint();
    }

    public AktørId hentAktørId() {
        return getForObject(cfg.aktørURI(), AktørId.class, true);
    }

    @Cacheable(cacheNames = "aktør")
    public String hentNavn(AktørId aktørId) {
        var navn = getForObject(cfg.personNavnURI(aktørId), Navn.class, false);
        return Stream.of(navn.fornavn(), navn.mellomnavn(), navn.etternavn())
            .filter(Objects::nonNull)
            .collect(Collectors.joining(" "));
    }

    @Cacheable(cacheNames = "organisasjon")
    public String orgNavn(String orgnr) {
        return getForObject(cfg.orgNavnURI(orgnr));
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
