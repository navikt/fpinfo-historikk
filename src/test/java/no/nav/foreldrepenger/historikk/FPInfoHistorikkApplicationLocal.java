package no.nav.foreldrepenger.historikk;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;

import java.io.IOException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class FPInfoHistorikkApplicationLocal {
    public static void main(String[] args) throws IOException {
        new SpringApplicationBuilder(FPInfoHistorikkApplicationLocal.class)
                .profiles(DEV)
                .main(FPInfoHistorikkApplicationLocal.class)
                .run(args);
    }
}
