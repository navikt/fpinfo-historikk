package no.nav.foreldrepenger.historikk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;

@Configuration
public class TxConfiguration {

    public static final String KAFKA_TM = "kafkaTM";
    public static final String JPA_TM = "jpaTM";

    @Primary
    @Bean(name = "transactionManager")
    public ChainedTransactionManager chainedTM(JpaTransactionManager jpaTM,
            KafkaTransactionManager<String, String> kafkaTM) {
        return new ChainedTransactionManager(kafkaTM, jpaTM);
    }

    @Bean(name = KAFKA_TM)
    public KafkaTransactionManager<String, String> kafkaTM(ProducerFactory<String, String> pf) {
        KafkaTransactionManager<String, String> tm = new KafkaTransactionManager<>(pf);
        tm.setNestedTransactionAllowed(true);
        return tm;
    }

    @Bean(name = JPA_TM)
    public JpaTransactionManager jpaTM() {
        return new JpaTransactionManager();
    }

    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> cf, KafkaTransactionManager<String, String> tm) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        factory.getContainerProperties().setTransactionManager(tm);
        return factory;
    }
}
