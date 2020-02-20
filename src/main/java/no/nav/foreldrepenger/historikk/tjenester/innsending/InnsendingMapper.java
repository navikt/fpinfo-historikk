package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingType.LASTET_OPP;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingType.SEND_SENERE;
import static no.nav.foreldrepenger.historikk.util.StreamUtil.safeStream;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.historikk.tjenester.dittnav.BeskjedDTO;

public final class InnsendingMapper {

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
        innslag.setInnsendt(i.getInnsendt());
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

    public static BeskjedDTO tilBeskjedDTO(InnsendingHendelse h, String url) {
        return new BeskjedDTO(h.getFnr().getFnr(), h.getSaksnummer(), 3, url,
                "Vi mottok din " + h.getHendelse().beskrivelse,
                null, h.getReferanseId());
    }
}
