package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingHendelse;
import no.nav.security.token.support.core.api.Unprotected;

@RestController
@ConditionalOnNotProd
@RequestMapping(path = DittNavDevController.DEVPATH, produces = APPLICATION_JSON_VALUE)
@Unprotected
public class DittNavDevController {
    static final String DEVPATH = "dittnav/dev";
    private final DittNav dittNav;

    DittNavDevController(DittNav dittNav) {
        this.dittNav = dittNav;
    }

    /*
     * @PostMapping("/avsluttOppgave/{fnr}/{grupperingsId}/{eventId}")
     * 
     * @ApiOperation("Avslutt oppgave i Ditt Nav via Kafka") public void
     * avsluttOppgave(@PathVariable("fnr") Fødselsnummer fnr,
     * 
     * @PathVariable("grupperingsId") String grupperingsId,
     * 
     * @PathVariable("eventId") String eventId) { dittNav.avsluttOppgave(fnr,
     * grupperingsId, eventId); }
     */

    /*
     * @PostMapping("/opprettOppgave")
     * 
     * @ApiOperation("Opprett oppgave i Ditt Nav via Kafka") public void
     * opprettOppgave(@RequestBody TilbakekrevingHendelse h) {
     * dittNav.opprettOppgave(h.getFnr(), h.getSaksnummer(), h.getDialogId(),
     * "opprett", h.getHendelse()); }
     */

    @PostMapping("/opprettBeskjed")
    @ApiOperation("Opprett beskjed i Ditt Nav via Kafka")
    public void opprettBeskjed(@RequestBody InnsendingHendelse h) {
        dittNav.opprettBeskjed(h.getFnr(), h.getSaksnummer(), UUID.randomUUID().toString(), "opprett", h.getHendelse());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dittNav=" + dittNav + "]";
    }

}
