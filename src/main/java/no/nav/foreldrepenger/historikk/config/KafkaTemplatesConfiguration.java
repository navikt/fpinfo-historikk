package no.nav.foreldrepenger.historikk.config;

import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import no.nav.brukernotifikasjon.schemas.input.NokkelInput;
import no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavProducerConfig;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Configuration
public class KafkaTemplatesConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaTemplatesConfiguration.class);

    private static final String CLIENT_ID = "fpinfo-historikk";
    private static final String CREDENTIALS_SOURCE = "USER_INFO";
    private static final String TX_ID = String.format("tx-%s-", UUID.randomUUID().toString());


    @Bean
    public ConsumerFactory<Object, Object> onPremConsumerFactory(OnpremKafkaConfig config) {
        var props = new HashMap<String, Object>();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, config.consumerGroupId());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, config.consumerEnableAutoCommit());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, config.autoOffsetReset());
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, config.securityProtocol());
        props.put(SaslConfigs.SASL_MECHANISM, config.saslMechanism());
        props.put(SaslConfigs.SASL_JAAS_CONFIG, config.jaasConfig());
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public KafkaOperations<String, String> onPremProducerTemplate(ProducerFactory<String, String> pf) {
        return new KafkaTemplate<>(pf);
    }

    @Bean
    public ProducerFactory<Object, Object> onPremProducerFactory(OnpremKafkaConfig config) {
        var props = new HashMap<String, Object>();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers());
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, config.securityProtocol());
        props.put(SaslConfigs.SASL_MECHANISM, config.saslMechanism());
        props.put(SaslConfigs.SASL_JAAS_CONFIG, config.jaasConfig());

        LOG.info("Kafka producer TRANSACTIONAL_ID_CONFIG: {}", TX_ID);
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, TX_ID);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaOperations<NokkelInput, Object> aivenProducerKafkaTemplate(DittNavProducerConfig config) {
        ProducerFactory<NokkelInput, Object> producerFactory = new DefaultKafkaProducerFactory<>(producerConfig(config));
        return new KafkaTemplate<>(producerFactory);
    }

    private static Map<String, Object> producerConfig(DittNavProducerConfig config) {
        var props = new HashMap<String, Object>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, config.schemaRegistryUri());
        props.put(KafkaAvroSerializerConfig.BASIC_AUTH_CREDENTIALS_SOURCE, CREDENTIALS_SOURCE);
        props.put(KafkaAvroSerializerConfig.USER_INFO_CONFIG, config.schemaRegistryUserInfo());
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
