package no.nav.foreldrepenger.historikk.oppslag;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    @GetMapping("/ping")
    @Unprotected
    public String ping(@RequestParam(name = "navn", defaultValue = "jordboer") String navn) throws IOException {
        LOG.info("Jeg ble pinget");
        if (Files.exists(Paths.get("/var/run/secrets/nais.io/vault/vault_token"))) {
            byte[] encoded = Files.readAllBytes(Paths.get("/var/run/secrets/nais.io/vault/vault_token"));
            return new String(encoded, "UTF-8").trim();
        }
        return "Hallo " + navn + " fra ubeskyttet ressurs";
    }
}
