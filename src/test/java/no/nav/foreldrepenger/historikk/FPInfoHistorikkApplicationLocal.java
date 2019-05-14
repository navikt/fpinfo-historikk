package no.nav.foreldrepenger.historikk;

import java.io.IOException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import no.nav.foreldrepenger.historikk.oppslag.VaultUtils;

@SpringBootApplication
public class FPInfoHistorikkApplicationLocal {
    public static void main(String[] args) throws IOException {
        new SpringApplicationBuilder(FPInfoHistorikkApplicationLocal.class)
                .profiles("dev", "preprod")
                .properties("spring.cloud.vault.token=" + VaultUtils.getToken())
                .main(FPInfoHistorikkApplicationLocal.class)
                .run(args);
    }
}
