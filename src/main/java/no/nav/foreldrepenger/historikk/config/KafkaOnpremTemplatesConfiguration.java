package no.nav.foreldrepenger.historikk.config;

import java.util.HashMap;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import static no.nav.foreldrepenger.historikk.config.KafkaOnpremListenerConfiguration.ONPREM;

@Configuration
public class KafkaOnpremTemplatesConfiguration {
    private static final String DEFAULT_GROUP_ID = "group_id";

    @Bean
    @Qualifier(ONPREM)
    public ConsumerFactory<Object, Object> onPremConsumerFactory(KafkaOnpremConfig config) {
        var props = new HashMap<String, Object>();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, config.autoOffsetReset());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, DEFAULT_GROUP_ID);
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, config.securityProtocolConfig());
        props.put(SaslConfigs.SASL_MECHANISM, config.saslMechanism());
        props.put(SaslConfigs.SASL_JAAS_CONFIG, config.jaasConfig());
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    @Qualifier(ONPREM)
    public KafkaOperations<Object, Object> onPremProducerTemplate(@Qualifier(ONPREM) ProducerFactory<Object, Object> pf) {
        return new KafkaTemplate<>(pf);
    }

    @Bean
    @Qualifier(ONPREM)
    public ProducerFactory<Object, Object> onPremProducerFactory(KafkaOnpremConfig config) {
        var props = new HashMap<String, Object>();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers());
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, config.securityProtocolConfig());
        props.put(SaslConfigs.SASL_MECHANISM, config.saslMechanism());
        props.put(SaslConfigs.SASL_JAAS_CONFIG, config.jaasConfig());
        return new DefaultKafkaProducerFactory<>(props);
    }

}
