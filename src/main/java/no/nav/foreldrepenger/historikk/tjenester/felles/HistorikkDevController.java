package no.nav.foreldrepenger.historikk.tjenester.felles;

import static java.util.stream.Stream.concat;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.http.UnprotectedRestController;
import no.nav.foreldrepenger.historikk.tjenester.innsending.Innsending;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.Inntektsmelding;
import no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.Tilbakekreving;

@UnprotectedRestController(HistorikkDevController.DEVPATH)
public class HistorikkDevController {

    static final String DEVPATH = HistorikkController.HISTORIKK + "/dev";
    private final Innsending søknad;
    private final Inntektsmelding inntektsmelding;
    private final Tilbakekreving tilbakekreving;
    private final Innsending innsending;

    HistorikkDevController(Innsending søknad, Inntektsmelding inntektsmelding,
            Tilbakekreving tilbakekreving, Innsending innsending) {
        this.søknad = søknad;
        this.inntektsmelding = inntektsmelding;
        this.tilbakekreving = tilbakekreving;
        this.innsending = innsending;
    }

    @GetMapping
    public List<HistorikkInnslag> allHistorikkFor(@RequestParam("aktørId") AktørId id) {
        return concat(tilbakekreving.tilbakekrevinger(id, false).stream(),
                concat(inntektsmelding.inntektsmeldinger(id).stream(), søknad.innsendinger(id).stream()))
                        .sorted()
                        .toList();
    }

    @GetMapping("/vedlegg")
    public List<String> vedlegg(@RequestParam("fnr") Fødselsnummer fnr, @RequestParam("saksnummer") String saksnummer) {
        return innsending.vedleggsInfo(fnr, saksnummer).manglende();
    }
}
