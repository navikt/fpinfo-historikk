package no.nav.foreldrepenger.historikk.meldinger;

import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.meldinger.event.InnsendingEvent;
import no.nav.foreldrepenger.historikk.util.JacksonUtil;

@Service
public class SøknadMottattKonsument {
    private static final Logger LOG = LoggerFactory.getLogger(SøknadMottattKonsument.class);

    private final HistorikkTjeneste historikk;
    private final JacksonUtil mapper;

    public SøknadMottattKonsument(JacksonUtil mapper, HistorikkTjeneste historikk) {
        this.mapper = mapper;
        this.historikk = historikk;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.søknad_topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void listen(String json, @Header(required = false, value = NAV_CALL_ID) String callId) {
        LOG.info("Mottok melding om søknad {}", json);
        InnsendingEvent event = mapper.convertTo(json, InnsendingEvent.class);
        LOG.info("Melding om søknad konvertert til {}", event);
        LOG.info("Lagrer historikkinnslag fra hendelse {}", event);
        historikk.lagre(event);
        LOG.info("Lagret historikkinnslag fra hendelse OK ");
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [mapper=" + mapper + "]";
    }
}