package no.nav.foreldrepenger.historikk.tjenester.felles;

import static java.util.stream.Stream.concat;

import java.util.List;

import no.nav.foreldrepenger.historikk.tjenester.oppslag.Oppslag;
import no.nav.foreldrepenger.historikk.tjenester.tidslinje.TidslinjeTjeneste;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import no.nav.foreldrepenger.historikk.http.ProtectedRestController;
import no.nav.foreldrepenger.historikk.tjenester.innsending.Innsending;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.Inntektsmelding;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding. InntektsmeldingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.Tilbakekreving;
import no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.TilbakekrevingInnslag;

@ProtectedRestController(HistorikkController.HISTORIKK)
public class HistorikkController {

    public static final String HISTORIKK = "historikk";
    private static final Logger LOG = LoggerFactory.getLogger(HistorikkController.class);
    private final Innsending innsending;
    private final Inntektsmelding inntektsmelding;
    private final Tilbakekreving tilbakekreving;
    private final Oppslag oppslag;
    private final TidslinjeTjeneste tidslinjeTjeneste;

    HistorikkController(Innsending innsending,
                        Inntektsmelding inntektsmelding,
                        Tilbakekreving tilbakekreving,
                        Oppslag oppslag,
                        TidslinjeTjeneste tidslinjeTjeneste) {
        this.innsending = innsending;
        this.inntektsmelding = inntektsmelding;
        this.tilbakekreving = tilbakekreving;
        this.oppslag = oppslag;
        this.tidslinjeTjeneste = tidslinjeTjeneste;
    }

    @GetMapping("/me/søknader")
    public List<InnsendingInnslag> søknader() {
        LOG.info("Henter søknader for pålogget bruker");
        var aktørId = oppslag.aktørId();
        return innsending.innsendinger(aktørId);
    }

    @GetMapping("/me/manglendevedlegg")
    public List<String> manglendeVedlegg(@RequestParam(name = "saksnummer") String saksnummer) {
        LOG.info("Henter manglende vedlegg for pålogget bruker");
        var manglendeVedlegg = innsending.vedleggsInfo(saksnummer).manglende();
        tidslinjeTjeneste.testTidslinje(saksnummer);
        return manglendeVedlegg;
    }

    @GetMapping("/me/inntektsmeldinger")
    public List<InntektsmeldingInnslag> inntektsmeldinger() {
        LOG.info("Henter inntektsmeldinger for pålogget bruker");
        var aktørId = oppslag.aktørId();
        return inntektsmelding.inntektsmeldinger(aktørId);
    }

    @GetMapping("/me/minidialoger")
    public List<TilbakekrevingInnslag> tilbakekrevinger(@RequestParam(defaultValue = "true") boolean activeOnly) {
        LOG.info("Henter minidialoger for pålogget bruker");
        var aktørId = oppslag.aktørId();
        return tilbakekreving.tilbakekrevinger(aktørId, activeOnly);
    }

    @GetMapping("/me/minidialoger/spm")
    public List<TilbakekrevingInnslag> aktive() {
        LOG.info("Henter aktive minidialoger for pålogget bruker");
        var aktørId = oppslag.aktørId();
        return tilbakekreving.aktive(aktørId);
    }

    @GetMapping("/me/all")
    public List<HistorikkInnslag> allHistorikk() {
        LOG.info("Henter all historikk for pålogget bruker");
        return concat(tilbakekrevinger(false).stream(),
                concat(inntektsmeldinger().stream(), søknader().stream()))
                        .sorted()
                        .toList();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + ", inntektsmelding=" + inntektsmelding
                + ", tilbakekreving=" + tilbakekreving + "]";
    }

}
