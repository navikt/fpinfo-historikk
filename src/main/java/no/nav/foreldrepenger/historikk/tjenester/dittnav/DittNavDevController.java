package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.security.token.support.core.api.Unprotected;

@RestController
//@ConditionalOnDevOrLocal
@RequestMapping(path = DittNavDevController.DEVPATH, produces = APPLICATION_JSON_VALUE)
@Unprotected
@Api(description = "Send til Ditt NAV, kun for testing lokalt og i dev")
public class DittNavDevController {
    static final String DEVPATH = "dittnav/dev";
    private final DittNavMeldingProdusent dittNav;

    DittNavDevController(DittNavMeldingProdusent dittNav) {
        this.dittNav = dittNav;
    }

    @PostMapping("/opprettOppgave")
    @ApiOperation("Opprett oppgave i Ditt Nav via Kafka")
    public void opprettOppgave(@RequestBody OppgaveDTO dto) {
        dittNav.opprettOppgave(dto);
    }

    @PostMapping("/avsluttOppgave")
    @ApiOperation("Avslutt oppgave i Ditt Nav via Kafka")
    public void avsluttOppgave(@RequestBody DoneDTO dto) {
        dittNav.avsluttOppgave(dto);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dittNav=" + dittNav + "]";
    }
}
