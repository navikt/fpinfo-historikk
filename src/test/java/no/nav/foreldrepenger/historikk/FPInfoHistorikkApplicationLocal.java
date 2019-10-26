package no.nav.foreldrepenger.historikk;

import static no.nav.foreldrepenger.historikk.config.ClusterAwareSpringProfileResolver.profiles;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.ExcelImportør;

@SpringBootApplication
@EnableTransactionManagement
@EnableKafka
@EnableCaching
@EnableJpaAuditing
@Configuration
@ComponentScan(excludeFilters = { @Filter(type = ASSIGNABLE_TYPE, value = FPInfoHistorikkApplication.class) })
public class FPInfoHistorikkApplicationLocal {
    public static void main(String[] args) throws IOException {
        new SpringApplicationBuilder(FPInfoHistorikkApplicationLocal.class)
                .profiles(profiles())
                .main(FPInfoHistorikkApplicationLocal.class)
                .run(args);
    }

    @Component
    @ConditionalOnProperty(name = "import.enabled", havingValue = "true")
    public static class EventListenerExampleBean {
        private final ExcelImportør importer;
        private final String navn;

        public EventListenerExampleBean(ExcelImportør importer, @Value("${import.name}") String navn) {
            this.importer = importer;
            this.navn = navn;
        }

        private static final Logger LOG = LoggerFactory.getLogger(EventListenerExampleBean.class);

        @EventListener
        public void onApplicationEvent(ContextRefreshedEvent event) throws IOException {
            LOG.info("XXXXXXXXXXX");
            importer.importer(new FileSystemResource(navn));
        }
    }
}
