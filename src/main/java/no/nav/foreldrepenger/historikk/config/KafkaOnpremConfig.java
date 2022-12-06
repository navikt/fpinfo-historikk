package no.nav.foreldrepenger.historikk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("kafka.onprem")
public record KafkaOnpremConfig(
    String bootstrapServers,
    String autoOffsetReset,
    String securityProtocolConfig,
    String saslMechanism,
    String jaasConfig
) {
}
