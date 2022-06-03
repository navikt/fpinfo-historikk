package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("kafka.aiven")
public record DittNavProducerConfig(
    String bootstrapServers,
    String ca,
    String caPath,
    String certificate,
    String certificatePath,
    String credstorePassword,
    String keystorePath,
    String privateKey,
    String privateKeyPath,
    String schemaRegistryUri,
    String schemaRegistryUserInfo,
    String truststorePath,
    String securityProtocol,
    String saslMechanism,
    String keyStoreType,
    String trustStoreType,
    String keySerializer,
    String valueSerializer
) { }
