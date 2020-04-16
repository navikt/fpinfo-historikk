package no.nav.foreldrepenger.historikk;

import static no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType.TILBAKEKREVING_SPM;
import static no.nav.foreldrepenger.historikk.tjenester.felles.YtelseType.FP;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.kafka.test.utils.ContainerTestUtils.waitForAssignment;
import static org.springframework.kafka.test.utils.KafkaTestUtils.consumerProps;
import static org.springframework.kafka.test.utils.KafkaTestUtils.producerProps;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.TilbakekrevingHendelse;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
@EmbeddedKafka
@AutoConfigureJsonTesters
public class SpringKafkaReceiverTest {

    private static final String TOPIC = "tilbakekreving";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;
    BlockingQueue<ConsumerRecord<String, String>> records;
    KafkaMessageListenerContainer<String, String> container;

    private Producer<String, String> producer;

    @BeforeAll
    void setUp() {
        var configs = new HashMap<>(consumerProps("consumer", "false", embeddedKafkaBroker));
        var cf = new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new StringDeserializer());
        var containerProperties = new ContainerProperties(TOPIC);
        container = new KafkaMessageListenerContainer<>(cf, containerProperties);
        records = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<String, String>) records::add);
        container.start();
        waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
        var producerConfigs = new HashMap<>(producerProps(embeddedKafkaBroker));
        producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(),
                new StringSerializer()).createProducer();
    }

    @AfterAll
    void tearDown() {
        container.stop();
    }

    @Test
    public void testTilbakekreving() throws Exception {

        var h = new TilbakekrevingHendelse(Fødselsnummer.valueOf("01010111111"),
                "42",
                "142",
                "242",
                "342",
                TILBAKEKREVING_SPM,
                LocalDate.now().plusDays(1),
                LocalDateTime.now(), true,
                FP);

        producer.send(new ProducerRecord<>(TOPIC, "my-aggregate-id", mapper.writeValueAsString(h)));
        producer.flush();

        var record = records.poll(100, TimeUnit.MILLISECONDS);
        assertThat(record).isNotNull();
        assertThat(record.key()).isEqualTo("my-aggregate-id");
        assertTrue(reflectionEquals(mapper.readValue(record.value(), TilbakekrevingHendelse.class), h));
    }
}