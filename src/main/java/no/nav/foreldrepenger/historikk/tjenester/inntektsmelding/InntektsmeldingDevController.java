package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.Api;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.error.ApiError;
import no.nav.foreldrepenger.historikk.http.UnprotectedRestController;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkController;
import no.nav.security.token.support.core.api.Unprotected;

@Api(description = "Send og hent inntektsmeldingshendelser, kun for testing lokalt og i dev")
@UnprotectedRestController(InntektsmeldingDevController.DEVPATH)
@Unprotected
public class InntektsmeldingDevController {

    static final String DEVPATH = HistorikkController.HISTORIKK + "/dev";
    private final Inntektsmelding inntektsmelding;

    InntektsmeldingDevController(Inntektsmelding inntektsmelding) {
        this.inntektsmelding = inntektsmelding;

    }

    @PostMapping("/lagreInntektsmelding")
    public ResponseEntity<?> lagre(@RequestBody @Valid InntektsmeldingHendelse hendelse) {
        boolean lagret = inntektsmelding.lagre(hendelse);
        ResponseEntity<Object> created = status(CREATED).build();
        return lagret ? created : conflict(hendelse.getReferanseId());
    }

    @GetMapping("/inntektsmeldinger")
    public List<InntektsmeldingInnslag> inntektsmeldinger(@RequestParam("aktørId") AktørId id) {
        return inntektsmelding.inntektsmeldinger(id);
    }

    private static ResponseEntity<ApiError> conflict(String id) {
        return status(CONFLICT).body(new ApiError(CONFLICT, "referanseId " + id + " er allerede lagret"));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[inntektsmelding=" + inntektsmelding + "]";
    }

}
