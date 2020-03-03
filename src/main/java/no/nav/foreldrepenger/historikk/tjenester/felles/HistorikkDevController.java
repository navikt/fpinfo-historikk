package no.nav.foreldrepenger.historikk.tjenester.felles;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.boot.conditionals.ConditionalOnDev;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.tjenester.innsending.Innsending;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.Inntektsmelding;
import no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.Tilbakekreving;
import no.nav.security.token.support.core.api.Unprotected;

@RestController
@ConditionalOnDev
@RequestMapping(path = HistorikkDevController.DEVPATH, produces = APPLICATION_JSON_VALUE)
@Unprotected
public class HistorikkDevController {

    static final String DEVPATH = HistorikkController.HISTORIKK + "/dev";
    private final Innsending søknad;
    private final Inntektsmelding inntektsmelding;
    private final Tilbakekreving tilbakekreving;

    HistorikkDevController(Innsending søknad, Inntektsmelding inntektsmelding,
            Tilbakekreving tilbakekreving) {
        this.søknad = søknad;
        this.inntektsmelding = inntektsmelding;
        this.tilbakekreving = tilbakekreving;
    }

    @GetMapping
    public List<HistorikkInnslag> allHistorikkFor(@RequestParam("aktørId") AktørId id) {
        return concat(tilbakekreving.tilbakekrevinger(id, false).stream(),
                concat(inntektsmelding.inntektsmeldinger(id).stream(), søknad.innsendinger(id).stream()))
                        .sorted()
                        .collect(toList());
    }
}
