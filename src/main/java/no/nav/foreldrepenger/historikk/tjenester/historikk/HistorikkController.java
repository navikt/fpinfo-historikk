package no.nav.foreldrepenger.historikk.tjenester.historikk;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@RequestMapping(value = "/historikk")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class HistorikkController {

    private static final Logger LOG = LoggerFactory.getLogger(HistorikkController.class);

    private final HistorikkTjeneste historikk;

    HistorikkController(HistorikkTjeneste historikk) {
        this.historikk = historikk;
    }

    @GetMapping("/me")
    public List<HistorikkInnslag> hentHistorikk() {
        return historikk.hentMinHistorikk();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [historikk=" + historikk + "]";
    }
}
