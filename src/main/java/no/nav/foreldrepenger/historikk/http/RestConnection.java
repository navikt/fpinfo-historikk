package no.nav.foreldrepenger.historikk.http;

import java.net.URI;

import org.springframework.http.HttpHeaders;

public interface RestConnection {

    <T> T getForObject(URI uri, Class<T> responseType);

    <T> T postForEntity(URI uri, Object payload, Class<T> responseType);

    <T> T getForObject(URI uri, Class<T> responseType, boolean doThrow);

    <T> T getForEntity(URI uri, Class<T> responseType);

    <T> T getForEntity(URI uri, Class<T> responseType, boolean doThrow);

    <T> T exchangeGet(URI uri, Class<T> responseType, HttpHeaders headers);

}
