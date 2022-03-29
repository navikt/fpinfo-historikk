package no.nav.foreldrepenger.historikk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionManager;

@Configuration
public class TestJpaTransactionManager {

    @Primary
    @Bean(name = "transactionManager")
    public TransactionManager testTM(JpaTransactionManager jpaTM) {
        return jpaTM;
    }


}
