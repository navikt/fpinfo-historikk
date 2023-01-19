package no.nav.foreldrepenger.historikk.tjenester.oppslag;


import no.nav.foreldrepenger.historikk.domain.AktørId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OppslagConnection {

    private static final Logger LOG = LoggerFactory.getLogger(OppslagConnection.class);

    private final OppslagConnectionRestTemplate clientLegacy;
    private final OppslagConnectionWebclient webClient;

    public OppslagConnection(OppslagConnectionRestTemplate clientLegacy,
                             OppslagConnectionWebclient webclient) {
        this.clientLegacy = clientLegacy;
        this.webClient = webclient;
    }
    public AktørId hentAktørId() {
        var aktørId = clientLegacy.hentAktørId();
        sjekkNy(aktørId);
        return aktørId;
    }

    public String orgNavn(String orgnr) {
        var orgNavn = clientLegacy.orgNavn(orgnr);
        sjekkNy(orgnr, orgNavn);
        return orgNavn;
    }

    private void sjekkNy(AktørId aktørId) {
        try {
            var nyAktørId = webClient.hentAktørId();
            var erLik = Objects.equals(nyAktørId, aktørId);
            LOG.info("Sjekk gammel/ny aktørId gir samme resultat: {}", erLik);
        } catch (Exception e) {
            LOG.info("Sjekk gammel/ny aktørId ga exception", e);
        }
    }

    private void sjekkNy(String orgnr, String orgNavn) {
        try {
            var nyOrgnavn = webClient.orgNavn(orgnr);
            var erLik = Objects.equals(nyOrgnavn, orgNavn);
            LOG.info("Sjekk gammel/ny orgnavn gir samme resultat: {}", erLik);
        } catch (Exception e) {
            LOG.info("Sjekk gammel/ny orgnavn ga exception", e);
        }
    }

}
