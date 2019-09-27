package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.util.StreamUtil.safeStream;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.historikk.tjenester.innsending.dao.JPAInnsendingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.innsending.dao.JPAInnsendingVedlegg;

final class InnsendingMapper {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingMapper.class);

    private InnsendingMapper() {

    }

    static InnsendingInnslag tilInnslag(JPAInnsendingInnslag i) {
        LOG.info("Mapper fra innslag {}", i);
        InnsendingInnslag innslag = new InnsendingInnslag(i.getFnr(), i.getHendelse());
        innslag.setOpprettet(i.getOpprettet());
        innslag.setJournalpostId(i.getJournalpostId());
        innslag.setSaksnr(i.getSaksnr());
        innslag.setAktørId(i.getAktørId());
        innslag.setVedlegg(tilVedlegg(i));
        innslag.setBehandlingsdato(i.getBehandlingsdato());
        innslag.setReferanseId(i.getReferanseId());
        LOG.info("Mappet til innslag {}", innslag);
        return innslag;
    }

    private static List<String> tilVedlegg(JPAInnsendingInnslag innslag) {
        return safeStream(innslag.getVedlegg())
                .map(JPAInnsendingVedlegg::getVedleggId)
                .collect(toList());
    }

    static JPAInnsendingInnslag fraHendelse(InnsendingHendelse hendelse) {
        LOG.info("Mapper fra hendelse {}", hendelse);
        JPAInnsendingInnslag innslag = new JPAInnsendingInnslag();
        innslag.setAktørId(hendelse.getAktørId());
        innslag.setReferanseId(hendelse.getReferanseId());
        innslag.setFnr(hendelse.getFnr());
        innslag.setSaksnr(hendelse.getSaksNr());
        innslag.setJournalpostId(hendelse.getJournalId());
        innslag.setHendelse(hendelse.getHendelse());
        innslag.setBehandlingsdato(hendelse.getFørsteBehandlingsdato());
        safeStream(hendelse.getVedlegg())
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

    static List<InnsendingInnslag> tilInnslag(List<JPAInnsendingInnslag> innslag) {
        return safeStream(innslag)
                .map(InnsendingMapper::tilInnslag)
                .collect(toList());
    }
}
