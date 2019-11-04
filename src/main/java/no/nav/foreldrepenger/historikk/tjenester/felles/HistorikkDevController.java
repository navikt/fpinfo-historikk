package no.nav.foreldrepenger.historikk.tjenester.felles;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogTjeneste;
import no.nav.foreldrepenger.historikk.util.ConditionalOnDevOrLocal;
import no.nav.security.token.support.core.api.Unprotected;

@RestController
@ConditionalOnDevOrLocal
@RequestMapping(path = HistorikkDevController.DEVPATH, produces = APPLICATION_JSON_VALUE)
@Unprotected
public class HistorikkDevController {

    static final String DEVPATH = HistorikkController.HISTORIKK + "/dev";
    private final InnsendingTjeneste søknad;
    private final InntektsmeldingTjeneste inntektsmelding;
    private final MinidialogTjeneste minidialog;

    HistorikkDevController(InnsendingTjeneste søknad, InntektsmeldingTjeneste inntektsmelding,
            MinidialogTjeneste minidialog) {
        this.søknad = søknad;
        this.inntektsmelding = inntektsmelding;
        this.minidialog = minidialog;
    }

    @GetMapping
    public List<HistorikkInnslag> allHistorikkFor(@RequestParam("aktørId") AktørId id) {
        return concat(minidialog.dialoger(id, false).stream(),
                concat(inntektsmelding.inntektsmeldinger(id).stream(), søknad.innsendinger(id).stream()))
                        .sorted()
                        .collect(toList());
    }
}
