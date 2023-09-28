package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.common.util.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.historikk.config.KafkaListenerConfiguration.AIVEN;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.common.util.MDCUtil;
import no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNav;
import no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.Tilbakekreving;
import org.springframework.transaction.annotation.Transactional;

@Service
@ConditionalOnProperty(name = "historikk.innsending.søknad.enabled")
public class InnsendingHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingHendelseKonsument.class);

    private final Innsending innsending;
    private final Tilbakekreving tilbakekreving;
    private final DittNav dittNav;

    public InnsendingHendelseKonsument(Innsending innsending,
                                       Tilbakekreving tilbakekreving,
                                       DittNav dittNav) {
        this.innsending = innsending;
        this.tilbakekreving = tilbakekreving;
        this.dittNav = dittNav;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.innsending.søknad.topic}'}",
                   groupId = "#{'${historikk.innsending.søknad.group-id}'}",
                   containerFactory = AIVEN)
    public void behandle(@Payload @Valid InnsendingHendelse h,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partitionId,
                         @Header(KafkaHeaders.OFFSET) int offset) {
        MDCUtil.toMDC(NAV_CALL_ID, h.getReferanseId());
        LOG.info("Mottok innsendingshendelse fra {}, partition {}, offset {}, {}", topic, offset, partitionId, h);
        innsending.lagreEllerOppdater(h);
        if (h.erEttersending() && (h.getDialogId() != null)) {
            LOG.info("Dette er en ettersending fra en tilbakekrevingsdialog med dialogId {}", h.getDialogId());
            avsluttOppgave(h);
        }
    }

    private void avsluttOppgave(InnsendingHendelse h) {
        tilbakekreving.avsluttOppgave(h.getAktørId(), h.getDialogId());
        dittNav.avsluttOppgave(h.getDialogId());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + ", dialog=" + tilbakekreving + ", dittNav="
            + dittNav + "]";
    }

}
