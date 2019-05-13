package no.nav.foreldrepenger.historikk.config;

//@EnableKafka
//@Configuration
public class KafkaConsumerConfig {
    /*
     * @Value(value = "${kafka.bootstrapAddress}") private String bootstrapAddress;
     * private ConsumerFactory<String, String> consumerFactory(String groupId) {
     * Map<String, Object> props = new HashMap<>();
     * props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
     * props.put(GROUP_ID_CONFIG, groupId); props.put(KEY_DESERIALIZER_CLASS_CONFIG,
     * StringDeserializer.class); props.put(VALUE_DESERIALIZER_CLASS_CONFIG,
     * StringDeserializer.class); return new DefaultKafkaConsumerFactory<>(props); }
     * @Bean public ConcurrentKafkaListenerContainerFactory<String, String>
     * fooKafkaListenerContainerFactory() {
     * ConcurrentKafkaListenerContainerFactory<String, String> factory = new
     * ConcurrentKafkaListenerContainerFactory<>();
     * factory.setConsumerFactory(consumerFactory("foo")); return factory; }
     * @Bean public ConcurrentKafkaListenerContainerFactory<String, String>
     * barKafkaListenerContainerFactory() {
     * ConcurrentKafkaListenerContainerFactory<String, String> factory = new
     * ConcurrentKafkaListenerContainerFactory<>();
     * factory.setConsumerFactory(consumerFactory("bar")); return factory; }
     */
}
