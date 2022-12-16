package no.nav.foreldrepenger.historikk.tjenester.tilbakekreving;

import static no.nav.foreldrepenger.common.util.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.historikk.config.KafkaListenerConfiguration.AIVEN;
import static no.nav.foreldrepenger.historikk.config.KafkaOnpremListenerConfiguration.CFONPREM;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.common.util.MDCUtil;
import no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNav;

@Service
@ConditionalOnProperty(name = "historikk.tilbakekreving.enabled")
public class TilbakekrevingHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(TilbakekrevingHendelseKonsument.class);
    private final Tilbakekreving tilbakekreving;
    private final DittNav dittNav;

    public TilbakekrevingHendelseKonsument(Tilbakekreving tilbakekreving, DittNav dittNav) {
        this.tilbakekreving = tilbakekreving;
        this.dittNav = dittNav;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.tilbakekreving.topic}'}",
        groupId = "#{'${historikk.tilbakekreving.group-id}'}",
        containerFactory = CFONPREM)
    public void listen(@Payload @Valid TilbakekrevingHendelse h) {
        MDCUtil.toMDC(NAV_CALL_ID, h.getDialogId());
        LOG.info("Mottok tilbakekrevingshendelse {}", h);
        switch (h.getHendelse()) {
            case TILBAKEKREVING_SPM -> opprettOppgave(h);
            case TILBAKEKREVING_FATTET_VEDTAK, TILBAKEKREVING_SPM_LUKKET, TILBAKEKREVING_HENLAGT -> avsluttOppgave(h);
            default -> LOG.warn("Hendelsetype {} ikke støttet", h.getHendelse());
        }
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.tilbakekreving-aiven.topic}'}",
                   groupId = "#{'${historikk.tilbakekreving-aiven.group-id}'}",
                   containerFactory = AIVEN)
    public void aivenListen(@Payload @Valid TilbakekrevingHendelse h,
                            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partitionId,
                            @Header(KafkaHeaders.OFFSET) int offset) {
        MDCUtil.toMDC(NAV_CALL_ID, h.getDialogId());
        LOG.info("Mottok tilbakekrevingshendelse {}, partition {}, offset {}, {}", topic, partitionId, offset, h);
        switch (h.getHendelse()) {
            case TILBAKEKREVING_SPM -> opprettOppgave(h);
            case TILBAKEKREVING_FATTET_VEDTAK, TILBAKEKREVING_SPM_LUKKET, TILBAKEKREVING_HENLAGT -> avsluttOppgave(h);
            default -> LOG.warn("Hendelsetype {} ikke støttet", h.getHendelse());
        }
    }

    private void avsluttOppgave(TilbakekrevingHendelse h) {
        LOG.info("Avslutter oppgave i selvbetjening grunnet hendelse {}", h);
        tilbakekreving.avsluttOppgave(h);
    }

    private void opprettOppgave(TilbakekrevingHendelse h) {
        LOG.info("Oppretter oppgave i selvbetjening for hendelse {}", h);
        tilbakekreving.opprettOppgave(h);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[tilbakekreving=" + tilbakekreving + ", dittNav=" + dittNav + "]";
    }

}
