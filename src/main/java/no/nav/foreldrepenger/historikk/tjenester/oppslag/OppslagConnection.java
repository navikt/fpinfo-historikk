package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import java.net.URI;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public AktørId hentAktørIdPDL() {
        try {
            return getForObject(cfg.aktørURIPDL(), AktørId.class, true);
        } catch (Exception e) {
            LOG.warn("Feil ved oppslag av aktørid via PDL", e);
            return null;
        }
    }

    @Cacheable(cacheNames = "fnr")
    public Fødselsnummer hentFnr(AktørId aktørId) {
        return getForObject(cfg.fnrURI(aktørId), Fødselsnummer.class, true);
    }

    @Cacheable(cacheNames = "aktør")
    public String hentNavn(AktørId aktørId) {
        return hentNavn(hentFnr(aktørId));
    }

    @Cacheable(cacheNames = "fnr")
    private String hentNavn(Fødselsnummer fnr) {
        return getForObject(cfg.personNavnURI(fnr));
    }

    @Cacheable(cacheNames = "organisasjon")
    String orgNavn(String orgnr) {
        return getForObject(cfg.orgNavnURI(orgnr));
    }

    @Override
    public boolean isEnabled() {
        return cfg.isEnabled();
    }

    public Fødselsnummer hentFnrPDL(AktørId aktørId) {
        try {
            return getForObject(cfg.fnrURIPDL(aktørId), Fødselsnummer.class, true);
        } catch (Exception e) {
            LOG.warn("Feil ved oppslag av FNR via PDL", e);
            return null;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [cfg=" + cfg + "]";
    }

    public boolean isBrukPdl() {
        return cfg.isBrukPdl();
    }

    public String getNavnPDL(AktørId aktørId) {
        try {
            var navn = getForObject(cfg.personNavnPDLURI(aktørId), Navn.class, true);
            return Stream.of(navn.fornavn(), navn.mellomnavn(), navn.etternavn())
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(" "));

        } catch (Exception e) {
            LOG.warn("Feil ved oppslag av NAVN via PDL", e);
            return null;
        }
    }

}
