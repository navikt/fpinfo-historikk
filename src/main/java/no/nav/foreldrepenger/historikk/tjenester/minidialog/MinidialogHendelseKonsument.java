package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavMapper.url;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavOperasjoner;

@Service
public class MinidialogHendelseKonsument implements EnvironmentAware {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogHendelseKonsument.class);
    private final MinidialogTjeneste dialog;
    private final DittNavOperasjoner dittNav;
    private Environment env;

    public MinidialogHendelseKonsument(MinidialogTjeneste dialog, DittNavOperasjoner dittNav) {
        this.dialog = dialog;
        this.dittNav = dittNav;
    }

    @KafkaListener(topics = "#{'${historikk.kafka.topics.tilbakekreving}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    @Transactional
    public void listen(@Payload @Valid MinidialogHendelse h) {
        LOG.info("Mottok minidialoghendelse {}", h);
        switch (h.getHendelse()) {
        case TILBAKEKREVING_SPM:
            dialog.lagre(h);
            dittNav.opprettOppgave(h.getFnr(), h.getSaksnummer(), "TODO", url(h.getHendelse(), env),
                    h.getDialogId());
            break;
        case TILBAKEKREVING_SVAR:
            dialog.deaktiver(h.getAktørId(), h.getDialogId());
            dittNav.avsluttOppgave(h.getFnr(), h.getSaksnummer(), h.getDialogId());
            break;
        default:
            LOG.warn("Hendelsetype {} ikke støttet", h.getHendelse());
            break;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dialog=" + dialog + "]";
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;

    }
}