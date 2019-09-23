package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.historikk.tjenester.innsending.dao.JPAInnsendingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.innsending.dao.JPAInnsendingVedlegg;

final class InnsendingMapper {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingMapper.class);

    private InnsendingMapper() {

    }

    static InnsendingInnslag tilHistorikkInnslag(JPAInnsendingInnslag i) {
        LOG.info("Mapper fra innslag {}", i);
        InnsendingInnslag innslag = new InnsendingInnslag(i.getFnr(), i.getHendelse());
        innslag.setOpprettet(i.getOpprettet());
        innslag.setJournalpostId(i.getJournalpostId());
        innslag.setSaksnr(i.getSaksnr());
        innslag.setAktørId(i.getAktørId());
        innslag.setVedlegg(tilVedlegg(i));
        innslag.setBehandlingsdato(i.getBehandlingsdato());
        LOG.info("Mappet til innslag {}", innslag);
        return innslag;
    }

    private static List<String> tilVedlegg(JPAInnsendingInnslag innslag) {
        return innslag.getVedlegg()
                .stream()
                .map(JPAInnsendingVedlegg::getVedleggId)
                .collect(toList());
    }

    static JPAInnsendingInnslag fraHendelse(InnsendingInnsendingHendelse event) {
        LOG.info("Mapper fra hendelse {}", event);
        JPAInnsendingInnslag innslag = new JPAInnsendingInnslag();
        innslag.setAktørId(event.getAktørId());
        innslag.setFnr(event.getFnr());
        innslag.setSaksnr(event.getSaksNr());
        innslag.setJournalpostId(event.getJournalId());
        innslag.setHendelse(event.getHendelse());
        innslag.setBehandlingsdato(event.getFørsteBehandlingsdato());
        event.getVedlegg()
                .stream()
                .map(InnsendingMapper::fraVedlegg)
                .forEach(innslag::addVedlegg);
        LOG.info("Mappet til innslag {}", innslag);
        return innslag;
    }

    private static JPAInnsendingVedlegg fraVedlegg(String id) {
        JPAInnsendingVedlegg vedlegg = new JPAInnsendingVedlegg();
        vedlegg.setVedleggId(id);
        return vedlegg;
    }

    static List<InnsendingInnslag> konverterFra(List<JPAInnsendingInnslag> innslag) {
        return innslag
                .stream()
                .map(InnsendingMapper::tilHistorikkInnslag)
                .collect(toList());
    }
}
