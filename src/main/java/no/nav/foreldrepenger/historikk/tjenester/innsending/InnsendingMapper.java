package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.common.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingType.LASTET_OPP;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingType.SEND_SENERE;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class InnsendingMapper {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingMapper.class);

    private InnsendingMapper() {

    }

    static InnsendingInnslag tilInnslag(JPAInnsendingInnslag i) {
        LOG.trace("Mapper fra innslag {}", i);
        var innslag = new InnsendingInnslag(i.getHendelse());
        innslag.setFnr(i.getFnr());
        innslag.setOpprettet(i.getOpprettet());
        innslag.setJournalpostId(i.getJournalpostId());
        innslag.setSaksnr(i.getSaksnr());
        innslag.setAktørId(i.getAktørId());
        innslag.setVedlegg(tilVedlegg(i));
        innslag.setBehandlingsdato(i.getBehandlingsdato());
        innslag.setInnsendt(i.getInnsendt());
        innslag.setReferanseId(i.getReferanseId());
        LOG.trace("Mappet til innslag {}", innslag);
        return innslag;
    }

    static JPAInnsendingInnslag nyFra(InnsendingFordeltOgJournalførtHendelse h) {
        JPAInnsendingInnslag innslag = new JPAInnsendingInnslag();
        innslag.setReferanseId(h.getForsendelseId());
        return oppdaterFra(h, innslag);
    }

    static JPAInnsendingInnslag oppdaterFra(InnsendingFordeltOgJournalførtHendelse h,
            JPAInnsendingInnslag innslag) {
        innslag.setSaksnr(h.getSaksnr());
        innslag.setJournalpostId(h.getJournalpostId());
        return innslag;
    }

    static JPAInnsendingInnslag nyFra(InnsendingHendelse hendelse) {
        return oppdaterFra(hendelse, new JPAInnsendingInnslag());
    }

    static JPAInnsendingInnslag oppdaterFra(InnsendingHendelse hendelse, JPAInnsendingInnslag innslag) {
        innslag.setAktørId(hendelse.getAktørId());
        innslag.setFnr(hendelse.getFnr());
        innslag.setReferanseId(hendelse.getReferanseId());
        innslag.setSaksnr(hendelse.getSaksnummer());
        innslag.setJournalpostId(hendelse.getJournalpostId());
        innslag.setHendelse(hendelse.getHendelse());
        innslag.setBehandlingsdato(hendelse.getFørsteBehandlingsdato());
        innslag.setInnsendt(hendelse.getOpprettet());
        safeStream(hendelse.getOpplastedeVedlegg())
                .map(v -> fraVedlegg(v, LASTET_OPP))
                .forEach(innslag::addVedlegg);
        safeStream(hendelse.getIkkeOpplastedeVedlegg())
                .map(v -> fraVedlegg(v, SEND_SENERE))
                .forEach(innslag::addVedlegg);
        LOG.info("trace til innslag {}", innslag);
        return innslag;
    }

    private static List<Pair<String, InnsendingType>> tilVedlegg(JPAInnsendingInnslag innslag) {
        return safeStream(innslag.getVedlegg())
                .map(InnsendingMapper::tilVedlegg)
                .toList();
    }

    private static Pair<String, InnsendingType> tilVedlegg(JPAInnsendingVedlegg vedlegg) {
        return Pair.of(vedlegg.getVedleggId(), vedlegg.getInnsendingType());

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
                .toList();
    }

}
