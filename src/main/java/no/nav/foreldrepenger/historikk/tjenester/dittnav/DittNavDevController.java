package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingHendelse;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogHendelse;
import no.nav.security.token.support.core.api.Unprotected;

@RestController
//@ConditionalOnDevOrLocal
@RequestMapping(path = DittNavDevController.DEVPATH, produces = APPLICATION_JSON_VALUE)
@Unprotected
@Api(description = "Send til Ditt NAV, kun for testing lokalt og i dev")
public class DittNavDevController {
    static final String DEVPATH = "dittnav/dev";
    private final DittNavOperasjoner dittNav;
    private final DittNavUrlGenerator urlGenerator;

    DittNavDevController(DittNavOperasjoner dittNav, DittNavUrlGenerator urlGenerator) {
        this.dittNav = dittNav;
        this.urlGenerator = urlGenerator;
    }

    @PostMapping("/avsluttOppgave/{fnr}/{grupperingsId}/{eventId}")
    public void avsluttOppgave(@PathVariable("fnr") Fødselsnummer fnr,
            @PathVariable("grupperingsId") String grupperingsId,
            @PathVariable("eventId") String eventId) {
        dittNav.avsluttOppgave(fnr, grupperingsId, eventId);
    }

    @PostMapping("/opprettOppgave")
    @ApiOperation("Opprett oppgave i Ditt Nav via Kafka")
    public void opprettOppgave(@RequestBody MinidialogHendelse h) {
        dittNav.opprettOppgave(h.getFnr(), h.getSaksnummer(), "opprett", urlGenerator.url(h.getHendelse()),
                h.getDialogId());
    }

    @PostMapping("/opprettBeskjed")
    @ApiOperation("Opprett beskjed i Ditt Nav via Kafka")
    public void opprettBeskjed(@RequestBody InnsendingHendelse h) {
        dittNav.opprettBeskjed(h.getFnr(), h.getSaksnummer(), "opprett", urlGenerator.url(h.getHendelse()),
                h.getReferanseId());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dittNav=" + dittNav + "]";
    }

}
