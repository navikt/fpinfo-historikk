package no.nav.bris.kafka;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import no.nav.foreldrepenger.historikk.kafka.Receiver;
import no.nav.foreldrepenger.historikk.kafka.Sender;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@DirtiesContext
//@EmbeddedKafka(partitions = 1, topics = { SpringKafkaApplicationTest.HELLOWORLD_TOPIC })
public class SpringKafkaApplicationTest1 {

    static final String HELLOWORLD_TOPIC = "helloworld.t";

    @Autowired
    private Receiver receiver;

    @Autowired
    private Sender sender;

    @Test
    public void testReceive() throws Exception {
        sender.send("Hello Spring Kafka!");

        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        assertThat(receiver.getLatch().getCount()).isEqualTo(0);
    }
}
