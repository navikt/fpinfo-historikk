package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

@Service
public class OppslagTjeneste implements Oppslag {

    private static final Logger LOG = LoggerFactory.getLogger(OppslagTjeneste.class);

    private final OppslagConnection connection;

    public OppslagTjeneste(OppslagConnection connection) {
        this.connection = connection;
    }

    @Override
    public AktørId aktørId() {
        return sammenlign(connection.hentAktørId(), connection.hentAktørIdPDL());
    }

    @Override
    public Fødselsnummer fnr(AktørId aktørId) {
        return sammenlign(connection.hentFnr(aktørId), connection.hentFnrPDL(aktørId));
    }

    @Override
    public String personNavn(AktørId aktørId) {
        try {
            return sammenlign(connection.hentNavn(aktørId), connection.getNavnPDL(aktørId));
        } catch (HttpClientErrorException.Unauthorized e) {
            return null;
        }
    }

    @Override
    public String orgNavn(String orgnr) {
        try {
            return connection.orgNavn(orgnr);
        } catch (HttpClientErrorException.Unauthorized e) {
            return null;
        }
    }

    private <T> T sammenlign(T tps, T pdl) {
        String name = tps.getClass().getSimpleName();
        LOG.info("Sammenligner {}, PDL bruk er {}", name, connection.isBrukPdl());
        if (!tps.equals(pdl)) {
            LOG.warn("TPS-{} og PDL-{} er ulike, tps={}, pdl={}", name, name, tps, pdl);
        } else {
            LOG.info("TPS-{} og PDL-{} er like, {}", name, name, pdl);
        }
        return connection.isBrukPdl() ? pdl : tps;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[connection=" + connection + "]";
    }

}
