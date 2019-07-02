package no.nav.foreldrepenger.historikk.tjenester.historikk;

import static no.nav.foreldrepenger.historikk.config.Constants.SELVBETJENING;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@RequestMapping(value = HistorikkController.HISTORIKK)
@ProtectedWithClaims(issuer = SELVBETJENING, claimMap = { "acr=Level4" })
public class HistorikkController {

    public static final String HISTORIKK = "/historikk";

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
