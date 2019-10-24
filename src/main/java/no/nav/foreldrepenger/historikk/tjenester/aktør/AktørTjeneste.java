package no.nav.foreldrepenger.historikk.tjenester.aktør;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.domain.AktørId;

@Service
public class AktørTjeneste implements Aktør {

    private static final Logger LOG = LoggerFactory.getLogger(AktørTjeneste.class);

    private final AktørConnection connection;

    public AktørTjeneste(AktørConnection connection) {
        this.connection = connection;
    }

    @Override
    public String ping() {
        return connection.ping();
    }

    @Override
    public boolean isEnabled() {
        return connection.isEnabled();
    }

    @Override
    public AktørId aktørId() {
        return connection.aktørId();
    }

}
