package no.nav.foreldrepenger.historikk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;

@Configuration
public class JpaTxConfiguration {

    public static final String JPA_TM = "jpaTM";

    @Bean(name = JPA_TM)
    public JpaTransactionManager jpaTM() {
        return new JpaTransactionManager();
    }

}
