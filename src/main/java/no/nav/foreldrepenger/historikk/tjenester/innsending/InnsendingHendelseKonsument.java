package no.nav.foreldrepenger.historikk.tjenester.innsending;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMeldingProdusent;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogTjeneste;

@Service
public class InnsendingHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingHendelseKonsument.class);

    private final InnsendingTjeneste innsending;
    private final MinidialogTjeneste dialog;
    private final DittNavMeldingProdusent dittNav;

    public InnsendingHendelseKonsument(InnsendingTjeneste innsending, MinidialogTjeneste dialog,
            DittNavMeldingProdusent dittNav) {
        this.innsending = innsending;
        this.dialog = dialog;
        this.dittNav = dittNav;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.kafka.topics.søknad_topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void behandle(@Payload @Valid InnsendingHendelse h) {
        LOG.info("Mottok innsendingshendelse {}", h);
        if (innsending.lagre(h) && h.erEttersending() && (h.getDialogId() != null)) {
            dialog.deaktiver(h.getAktørId(), h.getDialogId());
        }
        if (!h.getIkkeOpplastedeVedlegg().isEmpty()) {
            // lag minidialoginnslag
        }
        switch (h.getHendelse()) {
        case INITIELL_ENGANGSSTØNAD:
            break;
        case INITIELL_FORELDREPENGER:
            dittNav.opprettBeskjed(h);
            break;
        case INITIELL_SVANGERSKAPSPENGER:
            break;
        case ENDRING_FORELDREPENGER:
            break;
        case ENDRING_SVANGERSKAPSPENGER:
            break;
        case ETTERSENDING_ENGANGSSTØNAD:
            break;
        case ETTERSENDING_FORELDREPENGER:
            break;
        case ETTERSENDING_SVANGERSKAPSPENGER:
            break;
        default:
            break;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + ", dialog=" + dialog + "]";
    }
}