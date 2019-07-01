package no.nav.foreldrepenger.historikk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.AfterRollbackProcessor;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultAfterRollbackProcessor;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class TxConfiguration implements KafkaListenerConfigurer {

    public static final String KAFKA_TM = "kafkaTM";
    public static final String JPA_TM = "jpaTM";

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Primary
    @Bean(name = "transactionManager")
    public ChainedTransactionManager chainedTM(JpaTransactionManager jpaTM,
            KafkaTransactionManager<Object, Object> kafkaTM) {
        return new ChainedTransactionManager(kafkaTM, jpaTM);
    }

    @Bean(name = KAFKA_TM)
    public KafkaTransactionManager<Object, Object> kafkaTM(ProducerFactory<Object, Object> pf) {
        KafkaTransactionManager<Object, Object> tm = new KafkaTransactionManager<>(pf);
        tm.setNestedTransactionAllowed(true);
        return tm;
    }

    @Bean(name = JPA_TM)
    public JpaTransactionManager jpaTM() {
        return new JpaTransactionManager();
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Object, Object>> kafkaListenerContainerFactory(
            ConsumerFactory<Object, Object> cf, KafkaTransactionManager<Object, Object> tm, ObjectMapper mapper,
            AfterRollbackProcessor<Object, Object> afterRollbackProcessor) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        factory.setMessageConverter(new StringJsonMessageConverter(mapper));
        factory.getContainerProperties().setTransactionManager(tm);
        factory.setAfterRollbackProcessor(afterRollbackProcessor);
        return factory;
    }

    @Bean
    public AfterRollbackProcessor<Object, Object> rollbackProcessor(KafkaTemplate<Object, Object> template) {
        return new DefaultAfterRollbackProcessor<>(new DeadLetterPublishingRecoverer(template), 1);
    }

    @Override
    public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
        registrar.setValidator(this.validator);
    }
}
