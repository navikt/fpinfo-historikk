package no.nav.foreldrepenger.historikk.tjenester.historikk;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.historikk.tjenester.historikk.dao.JPAHistorikkInnslag;
import no.nav.foreldrepenger.historikk.tjenester.historikk.dao.JPAHistorikkVedlegg;
import no.nav.foreldrepenger.historikk.tjenester.innsending.SøknadInnsendingHendelse;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogHendelse;

final class HistorikkMapper {

    private static final Logger LOG = LoggerFactory.getLogger(HistorikkMapper.class);

    private HistorikkMapper() {

    }

    static JPAHistorikkInnslag fraMinidialog(MinidialogHendelse innslag, String journalPostId) {
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
        innslag.setVedlegg(tilVedlegg(i));
        LOG.info("Mappet til innslag {}", innslag);
        return innslag;
    }

    private static List<String> tilVedlegg(JPAHistorikkInnslag innslag) {
        return innslag.getVedlegg()
                .stream()
                .map(JPAHistorikkVedlegg::getVedleggId)
                .collect(toList());
    }

    static JPAHistorikkInnslag fraEvent(SøknadInnsendingHendelse event) {
        LOG.info("Mapper fra event {}", event);
        JPAHistorikkInnslag innslag = new JPAHistorikkInnslag();
        innslag.setAktørId(event.getAktørId());
        innslag.setFnr(event.getFnr());
        innslag.setSaksnr(event.getSaksNr());
        innslag.setJournalpostId(event.getJournalId());
        innslag.setTekst(event.getHendelse().name());
        event.getVedlegg()
                .stream()
                .map(HistorikkMapper::fraVedlegg)
                .forEach(innslag::addVedlegg);
        LOG.info("Mappet til innslag {}", innslag);
        return innslag;
    }

    private static JPAHistorikkVedlegg fraVedlegg(String id) {
        JPAHistorikkVedlegg vedlegg = new JPAHistorikkVedlegg();
        vedlegg.setVedleggId(id);
        return vedlegg;
    }

    static List<HistorikkInnslag> konverterFra(List<JPAHistorikkInnslag> innslag) {
        return innslag
                .stream()
                .map(HistorikkMapper::tilHistorikkInnslag)
                .collect(toList());
    }
}
