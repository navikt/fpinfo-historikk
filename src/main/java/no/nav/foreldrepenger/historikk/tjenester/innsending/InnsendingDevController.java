package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.status;

import java.util.List;

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
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkController;
import no.nav.security.token.support.core.api.Unprotected;

@RestController
@Profile({ LOCAL, DEV })
@RequestMapping(path = InnsendingDevController.DEVPATH, produces = APPLICATION_JSON_VALUE)
@Unprotected
@Api(description = "Send og hent innsendingshendelser, kun for testing lokalt og i dev")
public class InnsendingDevController {
    static final String DEVPATH = HistorikkController.HISTORIKK + "/dev";
    private final InnsendingHendelseProdusent produsent;
    private final InnsendingTjeneste innsending;

    InnsendingDevController(InnsendingHendelseProdusent produsent, InnsendingTjeneste innsending) {
        this.produsent = produsent;
        this.innsending = innsending;
    }

    @PostMapping("/send")
    public void produserSøknad(@RequestBody InnsendingHendelse hendelse) {
        produsent.send(hendelse);
    }

    @PostMapping("/lagre")
    public ResponseEntity<?> lagreSøknad(@RequestBody InnsendingHendelse hendelse) {
        boolean lagret = innsending.lagre(hendelse);
        return lagret ? status(CREATED).build()
                : status(CONFLICT)
                        .body(new ApiError(CONFLICT,
                                "referanseId " + hendelse.getReferanseId() + " er allerede lagret"));
    }

    @GetMapping("/søknader")
    public List<InnsendingInnslag> søknader(@RequestParam("aktørId") AktørId id) {
        return innsending.innsendinger(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[produsent=" + produsent + ", innsending=" + innsending + "]";
    }

}
