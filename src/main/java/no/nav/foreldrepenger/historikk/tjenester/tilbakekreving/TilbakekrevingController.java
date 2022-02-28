package no.nav.foreldrepenger.historikk.tjenester.tilbakekreving;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import no.nav.foreldrepenger.historikk.http.ProtectedRestController;

@ProtectedRestController(TilbakekrevingController.MINIDIALOG)
public class TilbakekrevingController {

    static final String MINIDIALOG = "/minidialog";
    private final Tilbakekreving tilbakekreving;

    TilbakekrevingController(Tilbakekreving tilbakekreving) {
        this.tilbakekreving = tilbakekreving;
    }

    @GetMapping("/me")
    public List<TilbakekrevingInnslag> tilbakekrevinger(
            @RequestParam(name = "activeOnly", defaultValue = "true") boolean activeOnly) {
        return tilbakekreving.tilbakekrevinger(activeOnly);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [tilbakekreving=" + tilbakekreving + "]";
    }
}
