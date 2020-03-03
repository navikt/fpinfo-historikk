package no.nav.foreldrepenger.historikk.tjenester.felles;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static no.nav.foreldrepenger.historikk.config.Constants.SELVBETJENING;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.innsending.Innsending;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.Inntektsmelding;
import no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.TilbakekrevingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.Tilbakekreving;
import no.nav.security.token.support.core.api.ProtectedWithClaims;

@RestController
@RequestMapping(value = HistorikkController.HISTORIKK)
@ProtectedWithClaims(issuer = SELVBETJENING, claimMap = { "acr=Level4" })
public class HistorikkController {

    public static final String HISTORIKK = "historikk";
    private static final Logger LOG = LoggerFactory.getLogger(HistorikkController.class);
    private final Innsending innsending;
    private final Inntektsmelding inntektsmelding;
    private final Tilbakekreving tilbakekreving;

    HistorikkController(Innsending innsending, Inntektsmelding inntektsmelding,
            Tilbakekreving tilbakekreving) {
        this.innsending = innsending;
        this.inntektsmelding = inntektsmelding;
        this.tilbakekreving = tilbakekreving;
    }

    @GetMapping("/me/søknader")
    public List<InnsendingInnslag> søknader() {
        LOG.info("Henter søknader for pålogget bruker");
        return innsending.innsendinger();
    }

    @GetMapping("/me/inntektsmeldinger")
    public List<InntektsmeldingInnslag> inntektsmeldinger() {
        LOG.info("Henter inntektsmeldinger for pålogget bruker");
        return inntektsmelding.inntektsmeldinger();
    }

    @GetMapping("/me/minidialoger")
    public List<TilbakekrevingInnslag> dialoger(@RequestParam(defaultValue = "true") boolean activeOnly) {
        LOG.info("Henter minidialoger for pålogget bruker");
        return tilbakekreving.tilbakekrevinger(activeOnly);
    }

    @GetMapping("/me/minidialoger/spm")
    public List<TilbakekrevingInnslag> aktive() {
        LOG.info("Henter aktive minidialoger for pålogget bruker");
        return tilbakekreving.aktive();
    }

    @GetMapping("/me/all")
    public List<HistorikkInnslag> allHistorikk() {
        LOG.info("Henter all historikk for pålogget bruker");
        return concat(dialoger(false).stream(),
                concat(inntektsmeldinger().stream(), søknader().stream()))
                        .sorted()
                        .collect(toList());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + ", inntektsmelding=" + inntektsmelding
                + ", tilbakekreving=" + tilbakekreving + "]";
    }

}
