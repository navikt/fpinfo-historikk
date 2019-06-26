package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogMapper.journalpostFra;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.tjenester.historikk.HistorikkTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.JournalføringTjeneste;
import no.nav.foreldrepenger.historikk.util.JacksonUtil;

@Service
@Profile({ DEV, PREPROD })
public class MinidialogEventKonsument {
    private final MinidialogTjeneste minidialog;
    private final JournalføringTjeneste journalføring;
    private final HistorikkTjeneste historikk;
    private final JacksonUtil mapper;

    public MinidialogEventKonsument(HistorikkTjeneste historikk, MinidialogTjeneste minidialog,
            JournalføringTjeneste journalføring,
            JacksonUtil mapper) {
        this.historikk = historikk;
        this.minidialog = minidialog;
        this.journalføring = journalføring;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "#{'${historikk.kafka.meldinger.topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    @Transactional
    public void listen(String json) {
        MinidialogInnslag innslag = mapper.convertTo(json, MinidialogInnslag.class);
        minidialog.lagre(innslag);
        String journalPostId = journalføring.journalfør(journalpostFra(innslag), false);
        historikk.lagre(innslag, journalPostId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[minidialog=" + minidialog + ", mapper=" + mapper + "]";
    }
}