package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogController.MINIDIALOG;
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
@RequestMapping(path = MinidialogDevController.DEVPATH, produces = APPLICATION_JSON_VALUE)
@Unprotected
@Api(description = "Send og hent minidialoghendelser, kun for testing lokalt og i dev")
public class MinidialogDevController {
    static final String DEVPATH = MINIDIALOG + "/dev";
    private final MinidialogTjeneste minidialog;
    private final MinidialogHendelseProdusent produsent;

    MinidialogDevController(MinidialogTjeneste minidialog, MinidialogHendelseProdusent produsent) {
        this.minidialog = minidialog;
        this.produsent = produsent;
    }

    @PostMapping("/sendMinidialog")
    @ApiOperation("Send en minidialoghendelse via Kafka")
    public void sendMinidialog(@RequestBody MinidialogHendelse hendelse) {
        produsent.send(hendelse);
    }

    @GetMapping("/minidialoger")
    @ApiOperation("Hent alle minidialoger (spørsmål og svar) for en gitt aktør og status")
    public List<MinidialogInnslag> dialoger(@RequestParam("aktørId") AktørId aktørId,
            @RequestParam(name = "activeOnly", defaultValue = "true") boolean activeOnly) {
        return minidialog.dialoger(aktørId, activeOnly);
    }

    @GetMapping("/spm")
    @ApiOperation("Hent alle aktive minidialogspørsmål for en gitt aktør")
    public List<MinidialogInnslag> aktive(@RequestParam("aktørId") AktørId id) {
        return minidialog.aktive(id);
    }

    @PostMapping("/lagreMinidialog")
    @ApiOperation("Lagre en minidialoghendelse rett i databasen, utenom Kafka")
    public ResponseEntity<?> lagre(@RequestBody @Valid MinidialogHendelse hendelse) {
        boolean lagret = minidialog.lagre(hendelse);
        return lagret ? status(CREATED).build()
                : status(CONFLICT)
                        .body(new ApiError(CONFLICT,
                                "dialogId " + hendelse.getDialogId() + " er allerede lagret"));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[minidialog=" + minidialog + ", produsent=" + produsent + "]";
    }
}
