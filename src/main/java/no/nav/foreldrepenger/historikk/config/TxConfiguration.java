package no.nav.foreldrepenger.historikk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import no.nav.foreldrepenger.historikk.domain.Melding;

@Configuration
public class TxConfiguration {

    @Autowired
    private KafkaProperties props;

    @Primary
    @Bean(name = "transactionManager")
    public ChainedTransactionManager chainedTransactionManager(JpaTransactionManager jpaTransactionManager,
            KafkaTransactionManager kafkaTransactionManager) {
        return new ChainedTransactionManager(kafkaTransactionManager, jpaTransactionManager);
    }

    @Bean
    public KafkaTransactionManager kafkaTransactionManager(ProducerFactory<String, Melding> pf) {
        KafkaTransactionManager ktm = new KafkaTransactionManager(pf);
        ktm.setTransactionSynchronization(AbstractPlatformTransactionManager.SYNCHRONIZATION_ALWAYS);
        ktm.setNestedTransactionAllowed(true);
        return ktm;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> cf, KafkaTransactionManager tf) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        factory.getContainerProperties().setTransactionManager(tf);
        return factory;
    }

    @Bean
    public ProducerFactory<String, Melding> producerFactory() {
        DefaultKafkaProducerFactory<String, Melding> pf = new DefaultKafkaProducerFactory<>(
                props.buildProducerProperties());

        pf.setTransactionIdPrefix("tx.");
        return pf;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                props.buildConsumerProperties());
    }

    @Bean
    public KafkaTemplate<String, Melding> kafkaTemplate(ProducerFactory<String, Melding> pf) {
        return new KafkaTemplate<>(pf);
    }

    @Bean(name = "jpa")
    public JpaTransactionManager jpaTransactionManager() {
        return new JpaTransactionManager();
    }
}
