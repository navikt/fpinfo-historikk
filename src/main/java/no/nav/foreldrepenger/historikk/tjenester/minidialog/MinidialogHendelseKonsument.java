package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.errorhandling.UnexpectedResponseException;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;

@Service
@Profile({ LOCAL, DEV })
public class MinidialogHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogHendelseKonsument.class);
    private final MinidialogTjeneste dialog;

    public MinidialogHendelseKonsument(MinidialogTjeneste minidialog) {
        this.dialog = minidialog;
    }

    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    @Transactional
    public void listen(@Payload @Valid MinidialogHendelse hendelse) {
        LOG.info("Mottok hendelse {}", hendelse);
        dialog.lagre(hendelse, null);
    }

    private boolean skalJournalføre(HendelseType type) {
        return HendelseType.TILBAKEKREVING_SVAR.equals(type);
    }

    private static String header(HendelseType hendelseType) {
        switch (hendelseType) {
        case TILBAKEKREVING_SPM:
            return "Spørsmål fra saksbehandler";
        case TILBAKEKREVING_SVAR:
            return "Svar fra bruker";
        default:
            throw new UnexpectedResponseException("Uventet hendelsestype " + hendelseType);
        }

    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[minidialog=" + dialog + "]";
    }
}