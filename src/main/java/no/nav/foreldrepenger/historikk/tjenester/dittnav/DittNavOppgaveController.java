package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import no.nav.foreldrepenger.historikk.http.UnprotectedRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;


// Laget for å skru av oppgaver ved overgang til fp-oversikt
// blir tilgjengelig på /api/internal/oppgaver
@UnprotectedRestController("/internal/oppgaver")
public class DittNavOppgaveController {
    private static final Logger LOG = LoggerFactory.getLogger(DittNavOppgaveController.class);

    private final DittNav dittNav;
    private final JPADittNavOppgaverRepository repository;

    public DittNavOppgaveController(DittNav dittNav, JPADittNavOppgaverRepository repository) {
        this.dittNav = dittNav;
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<String> start(@RequestParam(required = false, defaultValue = "50") Integer antall) {
        var cutOff = LocalDateTime.now().minusDays(28).toLocalDate().atStartOfDay();
        var åpneOppgaver = repository.ikkeAvsluttedeOppgaver(cutOff, antall);
        LOG.info("Avslutter DittNav-oppgaver, kalt med batchstørrelse antall {}, fant {} oppgaver",
            antall, åpneOppgaver.size());
        åpneOppgaver.forEach(dittNav::avsluttOppgave);
        var respons = "avsluttet " + åpneOppgaver.size();
        return ResponseEntity.ok().body(respons);
    }

}
