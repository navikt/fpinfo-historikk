package no.nav.foreldrepenger.historikk.tjenester.tilbakekreving;

import static no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.TilbakekrevingController.MINIDIALOG;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.status;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.error.ApiError;
import no.nav.security.token.support.core.api.Unprotected;

@RestController
@ConditionalOnNotProd
@RequestMapping(path = TilbakekrevingDevController.DEVPATH, produces = APPLICATION_JSON_VALUE)
@Unprotected
@Api(description = "Send og hent minidialoghendelser, kun for testing lokalt og i dev")
public class TilbakekrevingDevController {
    static final String DEVPATH = MINIDIALOG + "/dev";
    private final TilbakekrevingTjeneste tilbakekreving;

    TilbakekrevingDevController(TilbakekrevingTjeneste tilbakekreving) {
        this.tilbakekreving = tilbakekreving;
    }

    @GetMapping("/tilbakekrevinger")
    @ApiOperation("Hent alle tilbakekrevinger (spørsmål og svar) for en gitt aktør og status")
    public List<TilbakekrevingInnslag> dialoger(@RequestParam("aktørId") AktørId aktørId,
            @RequestParam(name = "activeOnly", defaultValue = "true") boolean activeOnly) {
        return tilbakekreving.tilbakekrevinger(aktørId, activeOnly);
    }

    @GetMapping("/spm")
    @ApiOperation("Hent alle aktive tilbakekrevingsspørsmål for en gitt aktør")
    public List<TilbakekrevingInnslag> aktive(@RequestParam("aktørId") AktørId id) {
        return tilbakekreving.aktive(id);
    }

    @PostMapping("/lagre")
    @ApiOperation("Lagre en tilbakekreving rett i databasen, utenom Kafka")
    public ResponseEntity<?> lagre(@RequestBody @Valid TilbakekrevingHendelse hendelse) {
        boolean lagret = tilbakekreving.opprettOppgave(hendelse);
        return lagret ? status(CREATED).build()
                : status(CONFLICT)
                        .body(new ApiError(CONFLICT,
                                "dialogId " + hendelse.getDialogId() + " er allerede lagret"));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[tilbakekreving=" + tilbakekreving + "]";
    }
}
