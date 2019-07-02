package no.nav.foreldrepenger.historikk.tjenester.historikk;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.historikk.tjenester.historikk.dao.JPAHistorikkInnslag;
import no.nav.foreldrepenger.historikk.tjenester.innsending.SøknadInnsendingEvent;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogInnslag;

public final class HistorikkMapper {

    private static final Logger LOG = LoggerFactory.getLogger(HistorikkMapper.class);

    private HistorikkMapper() {

    }

    static JPAHistorikkInnslag fraMinidialog(MinidialogInnslag innslag, String journalPostId) {
        JPAHistorikkInnslag historikk = new JPAHistorikkInnslag();
        historikk.setAktørId(innslag.getAktørId());
        historikk.setFnr(innslag.getFnr());
        historikk.setTekst(innslag.getTekst());
        historikk.setSaksnr(innslag.getSaksnr());
        historikk.setJournalpostId(journalPostId);
        return historikk;
    }

    static HistorikkInnslag tilHistorikkInnslag(JPAHistorikkInnslag i) {
        LOG.info("Mapper fra innslag {}", i);
        HistorikkInnslag innslag = new HistorikkInnslag(i.getFnr(), i.getTekst());
        innslag.setOpprettet(i.getOpprettet());
        innslag.setJournalpostId(i.getJournalpostId());
        innslag.setSaksnr(i.getSaksnr());
        innslag.setAktørId(i.getAktørId());
        LOG.info("Mappet til innslag {}", innslag);
        return innslag;
    }

    static JPAHistorikkInnslag fraEvent(SøknadInnsendingEvent event) {
        LOG.info("Mapper fra event {}", event);
        JPAHistorikkInnslag innslag = new JPAHistorikkInnslag();
        innslag.setAktørId(event.getAktørId());
        innslag.setFnr(event.getFnr());
        innslag.setSaksnr(event.getSaksNr());
        innslag.setJournalpostId(event.getJournalId());
        innslag.setTekst(event.getHendelse().name());
        LOG.info("Mappet til innslag {}", innslag);
        return innslag;
    }

    static List<HistorikkInnslag> konverterFra(List<JPAHistorikkInnslag> innslag) {
        return innslag
                .stream()
                .map(HistorikkMapper::tilHistorikkInnslag)
                .collect(toList());
    }
}
