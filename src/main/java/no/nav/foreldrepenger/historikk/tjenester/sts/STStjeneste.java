package no.nav.foreldrepenger.historikk.tjenester.sts;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "historikk.sts.enabled")
public class STStjeneste {

    private final STSConnection connection;

    public STStjeneste(STSConnection connection) {
        this.connection = connection;
    }

    public String accessToken() {
        return connection.hentToken();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[connection=" + connection + "]";
    }
}
