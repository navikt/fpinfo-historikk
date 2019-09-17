package no.nav.foreldrepenger.historikk.tjenester.felles;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.søknad.SøknadTjeneste;
import no.nav.security.oidc.api.Unprotected;

@RestController
@Profile({ LOCAL, DEV })
@RequestMapping(path = HistorikkController.HISTORIKK + "/dev", produces = APPLICATION_JSON_VALUE)
@Unprotected
public class HistorikkDevController {

    public static final String HISTORIKK = "/historikk";

    private final SøknadTjeneste søknad;
    private final InntektsmeldingTjeneste inntektsmelding;
    private final MinidialogTjeneste minidialog;

    HistorikkDevController(SøknadTjeneste søknad, InntektsmeldingTjeneste inntektsmelding,
            MinidialogTjeneste minidialog) {
        this.søknad = søknad;
        this.inntektsmelding = inntektsmelding;
        this.minidialog = minidialog;
    }

    @GetMapping("/all")
    public List<? extends HistorikkInnslag> historikk(@RequestParam("fnr") Fødselsnummer fnr) {
        return concat(minidialog.hentAktiveDialoger(fnr).stream(),
                concat(inntektsmelding.hentInntektsmeldinger(fnr).stream(), søknad.hentSøknader(fnr).stream()))
                        .sorted()
                        .collect(toList());
    }
}
