package no.nav.foreldrepenger.historikk.http;

import java.net.URI;

public interface PingEndpointAware extends Pingable {

    URI pingEndpoint();

    String name();

}
