package no.nav.foreldrepenger.historikk;

import static no.nav.foreldrepenger.historikk.config.ClusterAwareSpringProfileResolver.profiles;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import no.nav.security.token.support.test.spring.TokenGeneratorConfiguration;

@SpringBootApplication
@ConfigurationPropertiesScan("no.nav.foreldrepenger.historikk")
@EnableTransactionManagement
@EnableKafka
@EnableJpaAuditing
@EnableCaching(proxyTargetClass = true)
@EnableAspectJAutoProxy
@Import(value = TokenGeneratorConfiguration.class)
@ComponentScan(excludeFilters = { @Filter(type = ASSIGNABLE_TYPE, value = FPInfoHistorikkApplication.class) })
public class FPInfoHistorikkApplicationLocal {
    public static void main(String[] args) {
        new SpringApplicationBuilder(FPInfoHistorikkApplicationLocal.class)
                .profiles(profiles())
                .main(FPInfoHistorikkApplicationLocal.class)
                .run(args);
    }
}
