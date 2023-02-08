package no.nav.foreldrepenger.historikk.tjenester.oppslag;


import no.nav.foreldrepenger.historikk.domain.AktørId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OppslagConnection {

    private static final Logger LOG = LoggerFactory.getLogger(OppslagConnection.class);
    private static final Logger SECURE_LOG = LoggerFactory.getLogger("secureLogger");

    private final OppslagConnectionRestTemplate clientLegacy;
    private final OppslagConnectionWebclient webClient;

    public OppslagConnection(OppslagConnectionRestTemplate clientLegacy,
                             OppslagConnectionWebclient webclient) {
        this.clientLegacy = clientLegacy;
        this.webClient = webclient;
    }
    public AktørId hentAktørId() {
        return webClient.hentAktørId();
    }

    public String orgNavn(String orgnr) {
        var orgNavn = clientLegacy.orgNavn(orgnr);
        sjekkNy(orgnr, orgNavn);
        return orgNavn;
    }

    private void sjekkNy(String orgnr, String orgNavn) {
        try {
            var nyOrgnavn = webClient.orgNavn(orgnr);
            var erLik = Objects.equals(nyOrgnavn, orgNavn);
            if (!erLik) {
                SECURE_LOG.warn("Orgnavn-oppslag på orgnr {}, resultat avviker mellom resttemplate {} og webclient {}",
                    orgnr, orgNavn, nyOrgnavn);
            }
        } catch (Exception e) {
            LOG.info("Sjekk gammel/ny orgnavn ga exception", e);
        }
    }

}
