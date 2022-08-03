package no.nav.foreldrepenger.historikk.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.CommonContainerStoppingErrorHandler;
import org.springframework.kafka.listener.CommonDelegatingErrorHandler;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.naming.AuthenticationException;
import java.time.Duration;

import static org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL_IMMEDIATE;

@Configuration
public class KafkaListenerConfiguration implements KafkaListenerConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerConfiguration.class);
    private static final Logger SECURE_LOG = LoggerFactory.getLogger("secureLogger");
    @Autowired
    private LocalValidatorFactoryBean validator;

    @Override
    public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
        registrar.setValidator(this.validator);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Object, Object>> kafkaListenerContainerFactory(
        ConsumerFactory<Object, Object> cf, ObjectMapper mapper) {
        var factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        factory.setMessageConverter(new StringJsonMessageConverter(mapper));
        factory.getContainerProperties().setAuthExceptionRetryInterval(Duration.ofSeconds(5L));
        factory.getContainerProperties().setAckMode(MANUAL_IMMEDIATE);
        factory.setCommonErrorHandler(errorHandler());
        return factory;
    }

    private static CommonErrorHandler errorHandler() {
        var defaultHandler = new DefaultErrorHandler((record, exception) -> {
            LOG.warn("Konsum av melding fra topic {} feilet, se secure log for detaljer.", record.topic(), exception);
            SECURE_LOG.warn("Konsum av melding fra topic {} feilet. Melding: {}", record.topic(), record);
        }, new FixedBackOff(1000L, 9L));
        var delegatingHandler = new CommonDelegatingErrorHandler(defaultHandler);
        delegatingHandler.addDelegate(AuthenticationException.class, new CommonContainerStoppingErrorHandler());
        delegatingHandler.setAckAfterHandle(false);
        return delegatingHandler;
    }

}
