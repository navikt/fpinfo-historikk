package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.historikk.http.AbstractRestConnection;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.net.URI;

@ConditionalOnNotProd
@Component
public class ArkivConnection extends AbstractRestConnection {

    private final ArkivOppslagConfig cfg;


    protected ArkivConnection(RestOperations restOperations, ArkivOppslagConfig config) {
        super(restOperations, config);
        this.cfg = config;
    }

    @Override
    public URI pingEndpoint() {
        return null;
    }

    public byte[] hentDok(String journalpostId, String dokumentInfoId) {
        var uri = cfg.hentDokumentUri(journalpostId, dokumentInfoId);
        return getForObject(uri, byte[].class);
    }

    public ArkivOppslagJournalposter journalposter(String ident) {
        if (ident == null) {
            throw new IllegalArgumentException("Mangler ident");
        }
        return postForEntity(cfg.dokumenter(), query(ident), ArkivOppslagJournalposter.class);
    }

    private static String query(String ident) {
        return String.format("""
          dokumentoversiktSelvbetjening(ident: %s, tema: [FOR]) {
            journalposter {
              journalpostId
              journalposttype
              journalstatus
              tittel
              eksternReferanseId
              relevanteDatoer {
                dato
                datotype
              }
              sak {
                fagsakId
                fagsaksystem
                sakstype
              }
              dokumenter {
                dokumentInfoId
                brevkode
                tittel
                dokumentvarianter {
                  variantformat
                  filtype
                  brukerHarTilgang
                }
              }
            }
          }""", ident);
    }
}
