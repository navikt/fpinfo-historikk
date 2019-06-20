package no.nav.foreldrepenger.historikk.meldinger.minidialog;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.domain.MinidialogInnslag;
import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@RequestMapping(value = "/minidialog")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class MinidialogController {

    private final MinidialogTjeneste minidialog;

    MinidialogController(MinidialogTjeneste minidialog) {
        this.minidialog = minidialog;
    }

    @GetMapping("/me")
    public List<MinidialogInnslag> hentMinidialog() {
        return minidialog.hentMineAktiveDialoger();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [minidialog=" + minidialog + "]";
    }
}
