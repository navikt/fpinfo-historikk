package no.nav.foreldrepenger.historikk.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import no.nav.brukernotifikasjon.schemas.input.NokkelInput;
import no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavProducerConfig;

@Configuration
public class KafkaTemplatesConfiguration {

    private static final String CLIENT_ID = "fpinfo-historikk";
    private static final String CREDENTIALS_SOURCE = "USER_INFO";
    private static final String DEFAULT_GROUP_ID = "group_id";

    @Bean
    public ConsumerFactory<Object, Object> onPremConsumerFactory(OnpremKafkaConfig config) {
        var props = new HashMap<String, Object>();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, config.consumerEnableAutoCommit());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, config.autoOffsetReset());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, DEFAULT_GROUP_ID);
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, config.securityProtocolConfig());
        props.put(SaslConfigs.SASL_MECHANISM, config.saslMechanism());
        props.put(SaslConfigs.SASL_JAAS_CONFIG, config.jaasConfig());
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public KafkaOperations<Object, Object> onPremProducerTemplate(ProducerFactory<Object, Object> pf) {
        return new KafkaTemplate<>(pf);
    }

    @Bean
    public ProducerFactory<Object, Object> onPremProducerFactory(OnpremKafkaConfig config) {
        var props = new HashMap<String, Object>();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers());
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, config.securityProtocolConfig());
        props.put(SaslConfigs.SASL_MECHANISM, config.saslMechanism());
        props.put(SaslConfigs.SASL_JAAS_CONFIG, config.jaasConfig());
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, config.transactionalIdPrefix());
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaOperations<NokkelInput, Object> aivenProducerKafkaTemplate(DittNavProducerConfig config) {
        ProducerFactory<NokkelInput, Object> producerFactory = new DefaultKafkaProducerFactory<>(aivenProducerConfig(config));
        return new KafkaTemplate<>(producerFactory);
    }

    private static Map<String, Object> aivenProducerConfig(DittNavProducerConfig config) {
        var props = new HashMap<String, Object>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, config.keySerializer());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, config.valueSerializer());
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, config.schemaRegistryUri());
        props.put(AbstractKafkaSchemaSerDeConfig.BASIC_AUTH_CREDENTIALS_SOURCE, CREDENTIALS_SOURCE);
        props.put(AbstractKafkaSchemaSerDeConfig.USER_INFO_CONFIG, config.schemaRegistryUserInfo());
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, config.securityProtocol());
        props.put(SaslConfigs.SASL_MECHANISM, config.saslMechanism());
        props.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, config.trustStoreType());
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, config.truststorePath());
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, config.credstorePassword());
        props.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, config.keyStoreType());
        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, config.keystorePath());
        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, config.credstorePassword());
        props.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");
        return props;
    }

}
