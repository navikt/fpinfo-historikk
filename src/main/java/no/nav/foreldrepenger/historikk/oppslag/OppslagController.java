package no.nav.foreldrepenger.historikk.oppslag;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bettercloud.vault.SslConfig;
import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import com.bettercloud.vault.response.LookupResponse;

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
        LOG.info("Jeg ble pinget {} {}", System.getenv().getOrDefault("VAULT_TOKEN", "intet token"),
                System.getenv().getOrDefault("VAULT_TOKEN_PATH", "ingen token path"));
        String token = getToken();
        VaultConfig vaultConfig = null;
        try {
            vaultConfig = new VaultConfig()
                    .address(System.getenv().getOrDefault("VAULT_ADDR", "https://vault.adeo.no"))
                    .token(token)
                    .openTimeout(5)
                    .readTimeout(30)
                    .sslConfig(new SslConfig().build())
                    .build();
        } catch (VaultException e) {
            LOG.warn("Oops", e);
            return "Could not instantiate the Vault REST client";
        }
        Vault vault = new Vault(vaultConfig);
        try {
            LookupResponse lookupSelf = vault.auth().lookupSelf();
            if (lookupSelf.isRenewable()) {
                return "token is renewable";
            }
            return "token is not renewable";
        } catch (VaultException e) {
            LOG.warn("Oops 1", e);
            if (e.getHttpStatusCode() == 403) {
                return "The application's vault token seems to be invalid";
            } else {
                return "Could not validate the application's vault token";
            }
        }
    }

    private String getToken() throws IOException {
        if (Paths.get("/var/run/secrets/nais.io/vault/vault_token").toFile().exists()) {
            byte[] encoded = Files.readAllBytes(Paths.get("/var/run/secrets/nais.io/vault/vault_token"));
            return new String(encoded, StandardCharsets.UTF_8).trim();
        }
        return null;
    }
}
