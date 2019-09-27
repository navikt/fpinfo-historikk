package no.nav.foreldrepenger.historikk.tjenester.felles;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static no.nav.foreldrepenger.historikk.config.Constants.SELVBETJENING;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogInnslag;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogTjeneste;
import no.nav.security.token.support.core.api.ProtectedWithClaims;

@RestController
@RequestMapping(value = HistorikkController.HISTORIKK)
@ProtectedWithClaims(issuer = SELVBETJENING, claimMap = { "acr=Level4" })
public class HistorikkController {

    public static final String HISTORIKK = "historikk";

    private final InnsendingTjeneste innsending;
    private final InntektsmeldingTjeneste inntektsmelding;
    private final MinidialogTjeneste minidialog;

    HistorikkController(InnsendingTjeneste innsending, InntektsmeldingTjeneste inntektsmelding,
            MinidialogTjeneste minidialog) {
        this.innsending = innsending;
        this.inntektsmelding = inntektsmelding;
        this.minidialog = minidialog;
    }

    @GetMapping("/me/søknader")
    public List<InnsendingInnslag> søknader() {
        return innsending.innsendinger();
    }

    @GetMapping("/me/inntektsmeldinger")
    public List<InntektsmeldingInnslag> inntektsmeldinger() {
        return inntektsmelding.inntektsmeldinger();
    }

    @GetMapping("/me/minidialoger")
    public List<MinidialogInnslag> dialoger(@RequestParam(defaultValue = "true") boolean activeOnly) {
        return minidialog.dialoger(activeOnly);
    }

    @GetMapping("/me/minidialoger/spm")
    public List<MinidialogInnslag> aktive() {
        return minidialog.aktive();
    }

    @GetMapping("/me/all")
    public List<HistorikkInnslag> historikk() {
        return concat(dialoger(false).stream(),
                concat(inntektsmeldinger().stream(), søknader().stream()))
                        .sorted()
                        .collect(toList());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + ", inntektsmelding=" + inntektsmelding
                + ", minidialog=" + minidialog + "]";
    }

}
