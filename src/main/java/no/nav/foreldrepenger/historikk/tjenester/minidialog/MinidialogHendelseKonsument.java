package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.OppslagTjeneste;

@Service
public class MinidialogHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogHendelseKonsument.class);
    private final MinidialogTjeneste dialog;
    private final OppslagTjeneste oppslag;

    public MinidialogHendelseKonsument(MinidialogTjeneste dialog, OppslagTjeneste oppslag) {
        this.dialog = dialog;
        this.oppslag = oppslag;
    }

    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    @Transactional
    public void listen(@Payload @Valid MinidialogHendelse hendelse) {
        LOG.info("Mottok hendelse om minidialog {}", hendelse);
        Fødselsnummer fnr = oppslag.fnr(hendelse.getAktørId());
        LOG.info("FNR er {}", fnr);
        switch (hendelse.getHendelseType()) {
        case TILBAKEKREVING_SPM:
            dialog.lagre(hendelse);
            break;
        case TILBAKEKREVING_SVAR:
            break;
        default:
            LOG.info("Hendelsetype {} ikke støttet", hendelse.getHendelseType());
            break;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dialog=" + dialog + "]";
    }
}