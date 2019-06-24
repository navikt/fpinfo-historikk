package no.nav.foreldrepenger.historikk.tjenester.sts;

import org.springframework.stereotype.Service;

@Service
public class STStjeneste {

    private final STSConnection connection;

    public STStjeneste(STSConnection connection) {
        this.connection = connection;
    }

    // @Cacheable("sts")
    public String accessToken() {
        return connection.hentToken();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[connection=" + connection + "]";
    }

}
