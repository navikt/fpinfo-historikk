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
import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@RequestMapping(value = SøknadsController.HISTORIKK)
@ProtectedWithClaims(issuer = SELVBETJENING, claimMap = { "acr=Level4" })
public class SøknadsController {

    public static final String HISTORIKK = "/historikk";

    private final SøknadsTjeneste søknad;
    private final InntektsmeldingTjeneste inntektsmelding;

    SøknadsController(SøknadsTjeneste søknad, InntektsmeldingTjeneste inntektsmelding) {
        this.søknad = søknad;
        this.inntektsmelding = inntektsmelding;
    }

    @GetMapping("/me")
    public List<SøknadsInnslag> hentSøknader() {
        return søknad.hentSøknader();
    }

    @GetMapping("/me/all")
    public List<? extends HistorikkInnslag> hentHistorikk() {
        return concat(inntektsmelding.hentInntektsmeldinger().stream(), søknad.hentSøknader().stream())
                .sorted()
                .collect(toList());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[søknad=" + søknad + ", inntektsmelding=" + inntektsmelding + "]";
    }
}
