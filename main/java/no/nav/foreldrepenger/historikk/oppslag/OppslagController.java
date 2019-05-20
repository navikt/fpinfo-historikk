package no.nav.foreldrepenger.historikk.oppslag;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.security.oidc.api.ProtectedWithClaims;
import no.nav.security.oidc.api.Unprotected;

@RequestMapping(path = OppslagController.OPPSLAG, produces = APPLICATION_JSON_VALUE)
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
@RestController
public class OppslagController {
    static final String OPPSLAG = "oppslag";
    private static final Logger LOG = LoggerFactory.getLogger(OppslagController.class);
    // @Autowired
    // private JdbcTemplate jdbcTemplate;

    @GetMapping("/ping")
    @Unprotected
    public String ping(@RequestParam(name = "navn", defaultValue = "jordboer") String navn) {
        // LOG.trace("SELECTING " + jdbcTemplate.getDataSource());
        return "OK";
        // jdbcTemplate.execute("SELECT 1 ");
        // return "OK";
    }
}
