package no.nav.foreldrepenger.historikk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("kafka.onprem")
@ConstructorBinding
public record OnpremKafkaConfig(
    String bootstrapServers,
    String consumerGroupId,
    boolean consumerEnableAutoCommit,
    String autoOffsetReset,
    String securityProtocol,
    String saslMechanism,
    String jaasConfig,
    String transactionalIdPrefix
) {
}
