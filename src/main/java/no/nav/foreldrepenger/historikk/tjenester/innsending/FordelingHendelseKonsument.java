package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.common.util.Constants.NAV_CALL_ID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.common.util.MDCUtil;

@Service
@ConditionalOnProperty(name = "historikk.innsending.fordeling.enabled")
public class FordelingHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(FordelingHendelseKonsument.class);

    private final Innsending innsending;

    public FordelingHendelseKonsument(Innsending innsending) {
        this.innsending = innsending;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.innsending.fordeling.topic}'}", groupId = "#{'${historikk.innsending.fordeling.group-id}'}")
    public void behandle(@Payload @Valid InnsendingFordeltOgJournalførtHendelse h) {
        MDCUtil.toMDC(NAV_CALL_ID, h.getForsendelseId());
        LOG.info("Mottok fordelingshendelse {}", h);
        // innsending.lagreEllerOppdater(h); // TODO: Finn ut hva som må gjøres for å fullføre dette. Hvis det ikke lar seg fullføre, fjern.
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + "]";
    }
}
