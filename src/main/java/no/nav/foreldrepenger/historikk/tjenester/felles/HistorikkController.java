package no.nav.foreldrepenger.historikk.tjenester.felles;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static no.nav.foreldrepenger.historikk.config.Constants.SELVBETJENING;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogInnslag;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.søknad.SøknadInnslag;
import no.nav.foreldrepenger.historikk.tjenester.søknad.SøknadTjeneste;
import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@RequestMapping(value = HistorikkController.HISTORIKK)
@ProtectedWithClaims(issuer = SELVBETJENING, claimMap = { "acr=Level4" })
public class HistorikkController {

    public static final String HISTORIKK = "/historikk";

    private final SøknadTjeneste søknad;
    private final InntektsmeldingTjeneste inntektsmelding;
    private final MinidialogTjeneste minidialog;

    HistorikkController(SøknadTjeneste søknad, InntektsmeldingTjeneste inntektsmelding, MinidialogTjeneste minidialog) {
        this.søknad = søknad;
        this.inntektsmelding = inntektsmelding;
        this.minidialog = minidialog;
    }

    @GetMapping("/me/søknader")
    public List<SøknadInnslag> søknader() {
        return søknad.søknader();
    }

    @GetMapping("/me/inntektsmeldinger")
    public List<InntektsmeldingInnslag> inntektsmeldinger() {
        return inntektsmelding.inntektsmeldinger();
    }

    @GetMapping("/me/minidialoger")
    public List<MinidialogInnslag> minidialoger(@RequestParam(defaultValue = "true") boolean activeOnly) {
        return minidialog.hentDialoger(activeOnly);
    }

    @GetMapping("/me/minidialoger/spm")
    public List<MinidialogInnslag> minidialogSpørsmål() {
        return minidialog.hentAktiveDialogSpørsmål();
    }

    @GetMapping("/me/all")
    public List<? extends HistorikkInnslag> historikk() {
        return concat(minidialoger(false).stream(),
                concat(inntektsmeldinger().stream(), søknader().stream()))
                        .sorted()
                        .collect(toList());
    }

}
