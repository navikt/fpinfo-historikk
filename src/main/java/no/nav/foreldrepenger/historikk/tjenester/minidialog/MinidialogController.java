package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.tjenester.sts.STStjeneste;
import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@RequestMapping(value = "/minidialog")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class MinidialogController {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogController.class);
    private final MinidialogTjeneste minidialog;
    private final STStjeneste sts;

    MinidialogController(MinidialogTjeneste minidialog, STStjeneste sts) {
        this.minidialog = minidialog;
        this.sts = sts;
    }

    @GetMapping("/me")
    public List<MinidialogInnslag> hentMinidialog() {
        try {
            LOG.info("Henter token");
            String token = sts.accessToken();
            LOG.info("Fikk token", token);
        } catch (Exception e) {
            LOG.info("Fikk ikke token", e);

        }
        return minidialog.hentMineAktiveDialoger();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [minidialog=" + minidialog + "]";
    }
}
