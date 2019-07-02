package no.nav.foreldrepenger.historikk.tjenester.historikk;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.security.oidc.api.Unprotected;

@RestController
@Profile({ DEV, PREPROD })
@RequestMapping(path = HistorikkController.HISTORIKK + "/preprod", produces = APPLICATION_JSON_VALUE)
@Unprotected
public class HistorikkPreprodController {
    private final HistorikkTjeneste historikk;

    HistorikkPreprodController(HistorikkTjeneste historikk) {
        this.historikk = historikk;
    }

    @GetMapping("/hent")
    public List<HistorikkInnslag> hentHistorikk(@RequestParam("fnr") Fødselsnummer fnr) {
        return historikk.hentHistorikk(fnr);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [historikk=" + historikk + "]";
    }
}
