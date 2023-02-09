package no.nav.foreldrepenger.historikk.tjenester.tilbakekreving;

import java.util.List;

import no.nav.foreldrepenger.historikk.tjenester.oppslag.Oppslag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import no.nav.foreldrepenger.historikk.http.ProtectedRestController;

@ProtectedRestController(TilbakekrevingController.MINIDIALOG)
public class TilbakekrevingController {

    static final String MINIDIALOG = "/minidialog";
    private final Tilbakekreving tilbakekreving;
    private final Oppslag oppslag;

    TilbakekrevingController(Tilbakekreving tilbakekreving,
                             Oppslag oppslag) {
        this.tilbakekreving = tilbakekreving;
        this.oppslag = oppslag;
    }

    @GetMapping("/me")
    public List<TilbakekrevingInnslag> tilbakekrevinger(
            @RequestParam(name = "activeOnly", defaultValue = "true") boolean activeOnly) {
        var aktørId = oppslag.aktørId();
        return tilbakekreving.tilbakekrevinger(aktørId, activeOnly);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [tilbakekreving=" + tilbakekreving + "]";
    }
}
