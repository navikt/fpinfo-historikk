package no.nav.foreldrepenger.historikk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.AfterRollbackProcessor;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.DefaultAfterRollbackProcessor;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class TxConfiguration {

    public static final String KAFKA_TM = "kafkaTM";
    public static final String JPA_TM = "jpaTM";

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
            ConsumerFactory<Object, Object> cf, KafkaTransactionManager<Object, Object> tm, ObjectMapper mapper) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        factory.setMessageConverter(new StringJsonMessageConverter(mapper));
        factory.getContainerProperties().setTransactionManager(tm);
        return factory;
    }

    @Bean
    public AfterRollbackProcessor<Object, Object> rollbackProcessor() {
        return new DefaultAfterRollbackProcessor<>((record, exception) -> {
        }, 1);
    }
}
