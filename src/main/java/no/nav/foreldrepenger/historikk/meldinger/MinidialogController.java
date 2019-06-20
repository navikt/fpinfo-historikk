package no.nav.foreldrepenger.historikk.meldinger;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.domain.MinidialogInnslag;
import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@RequestMapping(value = "/minidialog")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class MinidialogController {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogController.class);

    private final MinidialogTjeneste minidialog;

    MinidialogController(MinidialogTjeneste minidialog) {
        this.minidialog = minidialog;
    }

    @GetMapping("/me")
    public List<MinidialogInnslag> hentMinidialog() {
        List<MinidialogInnslag> dialoger = minidialog.hentMineAktiveDialoger();
        LOG.info("Hentet dialoger {}", dialoger);
        return dialoger;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [minidialog=" + minidialog + "]";
    }
}
