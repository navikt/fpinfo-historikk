package no.nav.foreldrepenger.historikk;

import java.io.IOException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class FPInfoHistorikkApplicationLocal {
    public static void main(String[] args) throws IOException {
        new SpringApplicationBuilder(FPInfoHistorikkApplicationLocal.class)
                .profiles("dev", "preprod")
                .main(FPInfoHistorikkApplicationLocal.class)
                .run(args);
    }
}
