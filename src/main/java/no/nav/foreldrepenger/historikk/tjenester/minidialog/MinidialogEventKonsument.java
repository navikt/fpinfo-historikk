package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static no.nav.foreldrepenger.historikk.config.Constants.NAV_CALL_ID;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import java.util.Collections;

import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.tjenester.journalføring.AvsenderMottaker;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.Bruker;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.IdType;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.JournalføringTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.Journalpost;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.JournalpostType;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.Sak;
import no.nav.foreldrepenger.historikk.util.JacksonUtil;

@Service
@Profile({ DEV, PREPROD })
public class MinidialogEventKonsument {
    private final MinidialogTjeneste minidialog;
    private final JournalføringTjeneste journalføring;
    private final JacksonUtil mapper;

    public MinidialogEventKonsument(MinidialogTjeneste minidialog, JournalføringTjeneste journalføring,
            JacksonUtil mapper) {
        this.minidialog = minidialog;
        this.journalføring = journalføring;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    @Transactional
    public void listen(String json, @Header(required = false, value = NAV_CALL_ID) String callId) {
        MinidialogInnslag innslag = mapper.convertTo(json, MinidialogInnslag.class);
        minidialog.lagre(innslag);
        journalføring.journalfør(journalpostFra(innslag), true);
    }

    private Journalpost journalpostFra(MinidialogInnslag innslag) {
        return new Journalpost(JournalpostType.UTGAAENDE,
                new AvsenderMottaker(innslag.getFnr(), IdType.FNR, "Spørsmål fra saksbehandler"),
                new Bruker(innslag.getFnr()), innslag.getHandling().tema(),
                "tittel",
                Collections.emptyList(), new Sak(innslag.getSaksnr()),
                Collections.emptyList());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[minidialog=" + minidialog + ", mapper=" + mapper + "]";
    }
}