package no.nav.foreldrepenger.historikk;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class FPInfoHistorikkApplicationLocal {
    public static void main(String[] args) {
        new SpringApplicationBuilder(FPInfoHistorikkApplicationLocal.class)
                .profiles("dev", "preprod")
                .main(FPInfoHistorikkApplicationLocal.class)
                .run(args);
    }
}
