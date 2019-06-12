package no.nav.foreldrepenger.historikk.config;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;

@Configuration
public class TxConfiguration {

    @Primary
    @Bean(name = "transactionManager")
    public ChainedTransactionManager chainedTM(JpaTransactionManager jpaTM,
            KafkaTransactionManager<String, String> kafkaTM) {
        return new ChainedTransactionManager(kafkaTM, jpaTM);
    }

    @Bean(name = "kafka")
    public KafkaTransactionManager<String, String> kafkaTM(ProducerFactory<String, String> pf) {
        KafkaTransactionManager<String, String> tm = new KafkaTransactionManager<>(pf);
        tm.setNestedTransactionAllowed(true);
        return tm;
    }

    @Bean(name = "jpa")
    public JpaTransactionManager jpaTM() {
        return new JpaTransactionManager();
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> cf, KafkaTransactionManager<String, String> tm) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        factory.getContainerProperties().setTransactionManager(tm);
        return factory;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory(KafkaProperties props) {
        DefaultKafkaProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(
                props.buildProducerProperties());
        pf.setTransactionIdPrefix("tx.");
        return pf;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> pf) {
        return new KafkaTemplate<>(pf);
    }
}
