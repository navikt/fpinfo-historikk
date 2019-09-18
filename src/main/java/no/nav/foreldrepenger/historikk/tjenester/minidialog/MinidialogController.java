package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@RequestMapping(value = MinidialogController.MINIDIALOG)
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class MinidialogController {

    static final String MINIDIALOG = "/minidialog";
    private final MinidialogTjeneste minidialog;

    MinidialogController(MinidialogTjeneste minidialog) {
        this.minidialog = minidialog;
    }

    @GetMapping("/me")
    public List<MinidialogInnslag> hentMinidialog(
            @RequestParam(name = "activeOnly", defaultValue = "true") boolean activeOnly) {
        return minidialog.hentDialoger(activeOnly);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [minidialog=" + minidialog + "]";
    }
}
