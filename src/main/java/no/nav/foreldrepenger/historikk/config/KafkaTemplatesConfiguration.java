package no.nav.foreldrepenger.historikk.config;

import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import no.nav.brukernotifikasjon.schemas.input.NokkelInput;
import no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavProducerConfig;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

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
    public KafkaOperations<String, String> onPremTemplate(ProducerFactory<String, String> pf) {
        return new KafkaTemplate<>(pf);
    }

    @Bean
    public ProducerFactory<NokkelInput, Object> aivenProducerTemplate(DittNavProducerConfig config) {
        return new DefaultKafkaProducerFactory<>(producerConfig(config));
    }

    @Bean
    public KafkaOperations<NokkelInput, Object> kafkaTemplate(ProducerFactory<NokkelInput, Object> factory) {
        return new KafkaTemplate<>(factory);
    }

    private static Map<String, Object> producerConfig(DittNavProducerConfig config) {
        var props = new HashMap<String, Object>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, config.schemaRegistryUri());
        props.put(KafkaAvroSerializerConfig.BASIC_AUTH_CREDENTIALS_SOURCE, CREDENTIALS_SOURCE);
        props.put(KafkaAvroSerializerConfig.USER_INFO_CONFIG, config.schemaRegistryUserInfo());
        LOG.info("Kafka producer TRANSACTIONAL_ID_CONFIG: {}", TX_ID);
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, TX_ID);
        if (config.securityEnabled()) {
            props.put(SaslConfigs.SASL_MECHANISM, config.saslMechanism());
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, config.securityProtocol());
            props.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, config.trustStoreType());
            props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, config.truststorePath());
            props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, config.credstorePassword());
            props.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, config.keyStoreType());
            props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, config.keystorePath());
            props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, config.credstorePassword());
            props.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");
        }
        return props;
    }

}
