package no.nav.foreldrepenger.historikk.tjenester.søknad;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static no.nav.foreldrepenger.historikk.config.Constants.SELVBETJENING;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogTjeneste;
import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@RequestMapping(value = SøknadController.HISTORIKK)
@ProtectedWithClaims(issuer = SELVBETJENING, claimMap = { "acr=Level4" })
public class SøknadController {

    public static final String HISTORIKK = "/historikk";

    private final SøknadTjeneste søknad;
    private final InntektsmeldingTjeneste inntektsmelding;
    private final MinidialogTjeneste minidialog;

    SøknadController(SøknadTjeneste søknad, InntektsmeldingTjeneste inntektsmelding, MinidialogTjeneste minidialog) {
        this.søknad = søknad;
        this.inntektsmelding = inntektsmelding;
        this.minidialog = minidialog;
    }

    @GetMapping("/me")
    public List<SøknadInnslag> hentSøknader() {
        return søknad.hentSøknader();
    }

    @GetMapping("/me/all")
    public List<? extends HistorikkInnslag> hentHistorikk() {
        return concat(minidialog.hentAktiveDialoger().stream(),
                concat(inntektsmelding.hentInntektsmeldinger().stream(), søknad.hentSøknader().stream()))
                        .sorted()
                        .collect(toList());
    }

}
