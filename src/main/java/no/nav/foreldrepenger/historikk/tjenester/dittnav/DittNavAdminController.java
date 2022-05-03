package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.security.token.support.core.api.Unprotected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavAdminController.ADMIN;


@RestController
@RequestMapping(ADMIN)
@Tag(name = "DittNav admincontroller", description = "Administrer DittNav-oppgaver")
@Unprotected
public class DittNavAdminController {

    public static final String ADMIN = "/internal/admin";
    private static final Logger SECURE_LOG = LoggerFactory.getLogger("secureLogger");
    final DittNavMeldingProdusent dittNavMeldingProdusent;

    final JPADittNavOppgaverRepository dao;

    public DittNavAdminController(DittNavMeldingProdusent dittNavMeldingProdusent, JPADittNavOppgaverRepository dao) {
        this.dittNavMeldingProdusent = dittNavMeldingProdusent;
        this.dao = dao;
    }

    @PostMapping
    public String verifiser(@RequestBody AvsluttOppgaverReq req) {
        SECURE_LOG.info("Test til securelog");
        var deployDato = LocalDateTime.of(2022, 3, 25, 11, 27);
        var referanseIder = dao.ikkeAvsluttedeOppgaver(deployDato, req.antall());
        referanseIder.forEach(dittNavMeldingProdusent::avsluttOppgave);
        return String.join("\n", referanseIder);
    }

    record AvsluttOppgaverReq (int antall) {}

}
