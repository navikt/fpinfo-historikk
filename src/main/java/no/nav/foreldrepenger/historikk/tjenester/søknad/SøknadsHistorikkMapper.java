package no.nav.foreldrepenger.historikk.tjenester.søknad;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.historikk.tjenester.søknad.dao.JPASøknadsHistorikkInnslag;
import no.nav.foreldrepenger.historikk.tjenester.søknad.dao.JPASøknadsHistorikkVedlegg;

final class SøknadsHistorikkMapper {

    private static final Logger LOG = LoggerFactory.getLogger(SøknadsHistorikkMapper.class);

    private SøknadsHistorikkMapper() {

    }

    static SøknadsHistorikkInnslag tilHistorikkInnslag(JPASøknadsHistorikkInnslag i) {
        LOG.info("Mapper fra innslag {}", i);
        SøknadsHistorikkInnslag innslag = new SøknadsHistorikkInnslag(i.getFnr(), i.getTekst());
        innslag.setOpprettet(i.getOpprettet());
        innslag.setJournalpostId(i.getJournalpostId());
        innslag.setSaksnr(i.getSaksnr());
        innslag.setAktørId(i.getAktørId());
        innslag.setVedlegg(tilVedlegg(i));
        innslag.setBehandlingsdato(i.getBehandlingsdato());
        LOG.info("Mappet til innslag {}", innslag);
        return innslag;
    }

    private static List<String> tilVedlegg(JPASøknadsHistorikkInnslag innslag) {
        return innslag.getVedlegg()
                .stream()
                .map(JPASøknadsHistorikkVedlegg::getVedleggId)
                .collect(toList());
    }

    static JPASøknadsHistorikkInnslag fraHendelse(SøknadsInnsendingHendelse event) {
        LOG.info("Mapper fra hendelse {}", event);
        JPASøknadsHistorikkInnslag innslag = new JPASøknadsHistorikkInnslag();
        innslag.setAktørId(event.getAktørId());
        innslag.setFnr(event.getFnr());
        innslag.setSaksnr(event.getSaksNr());
        innslag.setJournalpostId(event.getJournalId());
        innslag.setTekst(event.getHendelse().name());
        innslag.setBehandlingsdato(event.getFørsteBehandlingsdato());
        event.getVedlegg()
                .stream()
                .map(SøknadsHistorikkMapper::fraVedlegg)
                .forEach(innslag::addVedlegg);
        LOG.info("Mappet til innslag {}", innslag);
        return innslag;
    }

    private static JPASøknadsHistorikkVedlegg fraVedlegg(String id) {
        JPASøknadsHistorikkVedlegg vedlegg = new JPASøknadsHistorikkVedlegg();
        vedlegg.setVedleggId(id);
        return vedlegg;
    }

    static List<SøknadsHistorikkInnslag> konverterFra(List<JPASøknadsHistorikkInnslag> innslag) {
        return innslag
                .stream()
                .map(SøknadsHistorikkMapper::tilHistorikkInnslag)
                .collect(toList());
    }
}
