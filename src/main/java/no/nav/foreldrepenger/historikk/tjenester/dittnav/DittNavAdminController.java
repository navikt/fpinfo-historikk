package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.boot.conditionals.ConditionalOnNotProd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavAdminController.ADMIN;


@RestController
@RequestMapping(ADMIN)
@Tag(name = "DittNav admincontroller", description = "Administrer DittNav-oppgaver")
@ConditionalOnNotProd
public class DittNavAdminController {

    public static final String ADMIN = "/internal/admin";
    private static final Logger LOG = LoggerFactory.getLogger(DittNavAdminController.class);
    private static final Logger SECURE_LOG = LoggerFactory.getLogger("secureLogger");
    final DittNavMeldingsHistorikk lager;

    final JPADittNavOppgaverRepository dao;

    public DittNavAdminController(DittNavMeldingsHistorikk lager, JPADittNavOppgaverRepository dao) {
        this.lager = lager;
        this.dao = dao;
    }

    @GetMapping
    public String verifiser() {
        var deployDato = LocalDateTime.of(2022, 3, 25, 11, 27);
        var førsteOppgaver = dao.ikkeAvsluttedeOppgaver(deployDato, 5);
        SECURE_LOG.info("Test til secure log. Første internReferanseIds vedlagt", førsteOppgaver);
        return "Hei autentisert bruker";
    }

}
