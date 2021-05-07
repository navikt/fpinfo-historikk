package no.nav.foreldrepenger.historikk.http;

import java.net.URI;

import no.nav.security.token.support.client.core.ClientProperties;
import no.nav.security.token.support.client.spring.ClientConfigurationProperties;

public interface ClientPropertiesFinder {
    ClientProperties findProperties(ClientConfigurationProperties configs, URI uri);
}
