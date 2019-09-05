package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.historikk.config.Constants.SELVBETJENING;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.tjenester.søknad.SøknadsHistorikkInnslag;
import no.nav.foreldrepenger.historikk.tjenester.søknad.SøknadsHistorikkTjeneste;
import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@RequestMapping(value = HistorikkController.HISTORIKK)
@ProtectedWithClaims(issuer = SELVBETJENING, claimMap = { "acr=Level4" })
public class HistorikkController {

    public static final String HISTORIKK = "/historikk";

    private final SøknadsHistorikkTjeneste historikk;

    HistorikkController(SøknadsHistorikkTjeneste historikk) {
        this.historikk = historikk;
    }

    @GetMapping("/me")
    public List<SøknadsHistorikkInnslag> hentSøknadHistorikk() {
        return historikk.hentSøknadHistorikk();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [historikk=" + historikk + "]";
    }
}
