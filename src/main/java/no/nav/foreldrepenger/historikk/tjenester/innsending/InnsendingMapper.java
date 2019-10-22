package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingType.LASTET_OPP;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingType.SEND_SENERE;
import static no.nav.foreldrepenger.historikk.util.StreamUtil.safeStream;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class InnsendingMapper {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingMapper.class);

    private InnsendingMapper() {

    }

    static InnsendingInnslag tilInnslag(JPAInnsendingInnslag i) {
        LOG.info("Mapper fra innslag {}", i);
        var innslag = new InnsendingInnslag(i.getHendelse());
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
        var innslag = new JPAInnsendingInnslag();
        innslag.setAktørId(hendelse.getAktørId());
        innslag.setReferanseId(hendelse.getReferanseId());
        innslag.setSaksnr(hendelse.getSaksNr());
        innslag.setJournalpostId(hendelse.getJournalId());
        innslag.setHendelse(hendelse.getHendelseType());
        innslag.setBehandlingsdato(hendelse.getFørsteBehandlingsdato());
        safeStream(hendelse.getOpplastedeVedlegg())
                .map(v -> fraVedlegg(v, LASTET_OPP))
                .forEach(innslag::addVedlegg);
        safeStream(hendelse.getIkkeOpplastedeVedlegg())
                .map(v -> fraVedlegg(v, SEND_SENERE))
                .forEach(innslag::addVedlegg);
        LOG.info("Mappet til innslag {}", innslag);
        return innslag;
    }

    private static JPAInnsendingVedlegg fraVedlegg(String id, InnsendingType type) {
        var vedlegg = new JPAInnsendingVedlegg();
        vedlegg.setVedleggId(id);
        vedlegg.setInnsendingType(type);
        return vedlegg;
    }

    static List<InnsendingInnslag> tilInnslag(List<JPAInnsendingInnslag> innslag) {
        return safeStream(innslag)
                .map(InnsendingMapper::tilInnslag)
                .collect(toList());
    }
}
