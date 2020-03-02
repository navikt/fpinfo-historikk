package no.nav.foreldrepenger.historikk.tjenester.tilbakekreving;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.security.token.support.core.api.ProtectedWithClaims;

@RestController
@RequestMapping(value = TilbakekrevingController.MINIDIALOG)
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class TilbakekrevingController {

    static final String MINIDIALOG = "/minidialog";
    private final TilbakekrevingTjeneste minidialog;

    TilbakekrevingController(TilbakekrevingTjeneste minidialog) {
        this.minidialog = minidialog;
    }

    @GetMapping("/me")
    public List<TilbakekrevingInnslag> dialoger(
            @RequestParam(name = "activeOnly", defaultValue = "true") boolean activeOnly) {
        return minidialog.dialoger(activeOnly);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [minidialog=" + minidialog + "]";
    }
}
