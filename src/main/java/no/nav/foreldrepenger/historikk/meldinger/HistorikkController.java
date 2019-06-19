package no.nav.foreldrepenger.historikk.meldinger;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.domain.HistorikkInnslag;
import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@RequestMapping(value = "/historikk")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class HistorikkController {

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
