package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.security.token.support.core.api.Unprotected;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavAdminController.ADMIN;


@RestController
@RequestMapping(ADMIN)
@Tag(name = "DittNav admincontroller", description = "Administrer DittNav-oppgaver")
@Unprotected
@DependsOn("adminWebSecurityConfigurerAdapter")
public class DittNavAdminController {

    public static final String ADMIN = "/internal/admin";
    final DittNavMeldingProdusent dittNavMeldingProdusent;

    final JPADittNavOppgaverRepository dao;

    public DittNavAdminController(DittNavMeldingProdusent dittNavMeldingProdusent, JPADittNavOppgaverRepository dao) {
        this.dittNavMeldingProdusent = dittNavMeldingProdusent;
        this.dao = dao;
    }

    @PostMapping
    public String verifiser(@RequestBody AvsluttOppgaverReq req) {
        var deployDato = LocalDateTime.of(2022, 3, 25, 11, 27);
        var referanseIder = dao.ikkeAvsluttedeOppgaver(deployDato, req.antall());
        referanseIder.forEach(dittNavMeldingProdusent::avsluttOppgave);
        return String.join("\n", referanseIder);
    }

    record AvsluttOppgaverReq (int antall) {}

}
