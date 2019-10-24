package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.status;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.error.ApiError;
import no.nav.foreldrepenger.historikk.tjenester.aktør.AktørTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkController;
import no.nav.security.token.support.core.api.Unprotected;

@RestController
@Profile({ LOCAL, DEV })
@Api(description = "Send og hent inntektsmeldingshendelser, kun for testing lokalt og i dev")
@RequestMapping(path = InntektsmeldingDevController.DEVPATH, produces = APPLICATION_JSON_VALUE)
@Unprotected
public class InntektsmeldingDevController {

    @Inject
    private AktørTjeneste aktør;

    static final String DEVPATH = HistorikkController.HISTORIKK + "/dev";
    private final InntektsmeldingTjeneste inntektsmelding;
    private final InntektsmeldingHendelseProdusent produsent;

    InntektsmeldingDevController(InntektsmeldingTjeneste inntektsmelding,
            InntektsmeldingHendelseProdusent produsent) {
        this.inntektsmelding = inntektsmelding;
        this.produsent = produsent;

    }

    @PostMapping("/lagreInntektsmelding")
    public ResponseEntity<?> lagre(@RequestBody @Valid InntektsmeldingHendelse hendelse) {
        boolean lagret = inntektsmelding.lagre(hendelse);
        return lagret ? status(CREATED).build()
                : status(CONFLICT)
                        .body(new ApiError(CONFLICT,
                                "referanseId " + hendelse.getReferanseId() + " er allerede lagret"));
    }

    @PostMapping("/sendInntektsmelding")
    public void send(@RequestBody @Valid InntektsmeldingHendelse hendelse) {
        produsent.send(hendelse);
    }

    @GetMapping("/inntektsmeldinger")
    public List<InntektsmeldingInnslag> inntektsmeldinger(@RequestParam("aktørId") AktørId id) {
        return inntektsmelding.inntektsmeldinger(id);
    }

    @GetMapping("/ping")
    public String ping() {
        return aktør.ping();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[produsent=" + produsent + ", inntektsmelding=" + inntektsmelding + "]";
    }

}
