package no.nav.foreldrepenger.historikk.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.util.backoff.ExponentialBackOff;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.Duration;
import java.util.List;

import static org.springframework.kafka.listener.ContainerProperties.AckMode.BATCH;

@Configuration
public class KafkaOnpremListenerConfiguration implements KafkaListenerConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaOnpremListenerConfiguration.class);
    private static final Logger SECURE_LOG = LoggerFactory.getLogger("secureLogger");

    public static final String ONPREM = "onprem";

    private LocalValidatorFactoryBean validator;

    @Autowired
    public KafkaOnpremListenerConfiguration(LocalValidatorFactoryBean validator) {
        this.validator = validator;
    }

    @Override
    public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
        registrar.setValidator(this.validator);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Object, Object>> onpremKafkaListenerContainerFactory(
        @Qualifier(ONPREM) ConsumerFactory<Object, Object> cf,
        ObjectMapper mapper) {
        var factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        factory.setMessageConverter(new StringJsonMessageConverter(mapper));
        factory.getContainerProperties().setAuthExceptionRetryInterval(Duration.ofSeconds(5L));
        factory.getContainerProperties().setAckMode(BATCH);
        factory.setContainerCustomizer(container -> {
            final var containerStoppingErrorHandler = new CommonContainerStoppingErrorHandler();
            container.setCommonErrorHandler(new DefaultErrorHandler((record, exception) -> {
                LOG.warn("Kafkakonsumer avsluttet, må følges opp! Konsum av melding fra topic {} feilet, se secure log for detaljer.",
                    record.topic(), exception);
                SECURE_LOG.warn("Konsum av melding fra topic {} feilet. Melding: {}", record.topic(), record);
                containerStoppingErrorHandler.handleRemaining(exception, List.of(),
                    new MockConsumer<>(OffsetResetStrategy.NONE), container
                );
            }, exponentialBackoff()));
        });
        return factory;
    }

    private static ExponentialBackOff exponentialBackoff() {
        final var backoff = new ExponentialBackOff();
        backoff.setInitialInterval(300);
        backoff.setMultiplier(2);
        backoff.setMaxInterval(2_000);
        backoff.setMaxElapsedTime(25_000);
        return backoff;
    }

}
