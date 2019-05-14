package no.nav.foreldrepenger.historikk.oppslag;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class VaultUtils {
    private VaultUtils() {
    }

    public static String getToken() throws IOException {
        if (Paths.get("/var/run/secrets/nais.io/vault/vault_token").toFile().exists()) {
            byte[] encoded = Files.readAllBytes(Paths.get("/var/run/secrets/nais.io/vault/vault_token"));
            return new String(encoded, StandardCharsets.UTF_8).trim();
        }
        return null;
    }
}
