package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import io.swagger.v3.oas.annotations.tags.Tag;
import no.nav.foreldrepenger.boot.conditionals.ConditionalOnNotProd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavAdminController.ADMIN;


@RestController
@RequestMapping(ADMIN)
@Tag(name = "DittNav admincontroller", description = "Administrer DittNav-oppgaver")
@ConditionalOnNotProd
public class DittNavAdminController {

    public static final String ADMIN = "/internal/admin";
    private static final Logger LOG = LoggerFactory.getLogger(DittNavAdminController.class);
    private static final Logger SECURE_LOG = LoggerFactory.getLogger("secureLogger");


    @GetMapping
    public String verifiser() {
        SECURE_LOG.info("Test til secure log");
        return "Hei autentisert bruker";
    }

}
