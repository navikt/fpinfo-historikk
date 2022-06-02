package no.nav.foreldrepenger.historikk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kafka.onprem")
public record OnpremKafkaConfig(
    String bootstrapServers,
    boolean consumerEnableAutoCommit,
    String autoOffsetReset,
    String securityProtocolConfig,
    String saslMechanism,
    String jaasConfig,
    String transactionalIdPrefix
) {
}
