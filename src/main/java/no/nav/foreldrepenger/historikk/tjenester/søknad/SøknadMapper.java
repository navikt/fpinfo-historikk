package no.nav.foreldrepenger.historikk.tjenester.søknad;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.historikk.tjenester.søknad.dao.JPASøknadInnslag;
import no.nav.foreldrepenger.historikk.tjenester.søknad.dao.JPASøknadVedlegg;

final class SøknadMapper {

    private static final Logger LOG = LoggerFactory.getLogger(SøknadMapper.class);

    private SøknadMapper() {

    }

    static SøknadInnslag tilHistorikkInnslag(JPASøknadInnslag i) {
        LOG.info("Mapper fra innslag {}", i);
        SøknadInnslag innslag = new SøknadInnslag(i.getFnr(), i.getTekst());
        innslag.setOpprettet(i.getOpprettet());
        innslag.setJournalpostId(i.getJournalpostId());
        innslag.setSaksnr(i.getSaksnr());
        innslag.setAktørId(i.getAktørId());
        innslag.setVedlegg(tilVedlegg(i));
        innslag.setBehandlingsdato(i.getBehandlingsdato());
        LOG.info("Mappet til innslag {}", innslag);
        return innslag;
    }

    private static List<String> tilVedlegg(JPASøknadInnslag innslag) {
        return innslag.getVedlegg()
                .stream()
                .map(JPASøknadVedlegg::getVedleggId)
                .collect(toList());
    }

    static JPASøknadInnslag fraHendelse(SøknadInnsendingHendelse event) {
        LOG.info("Mapper fra hendelse {}", event);
        JPASøknadInnslag innslag = new JPASøknadInnslag();
        innslag.setAktørId(event.getAktørId());
        innslag.setFnr(event.getFnr());
        innslag.setSaksnr(event.getSaksNr());
        innslag.setJournalpostId(event.getJournalId());
        innslag.setTekst(event.getHendelse().name());
        innslag.setBehandlingsdato(event.getFørsteBehandlingsdato());
        event.getVedlegg()
                .stream()
                .map(SøknadMapper::fraVedlegg)
                .forEach(innslag::addVedlegg);
        LOG.info("Mappet til innslag {}", innslag);
        return innslag;
    }

    private static JPASøknadVedlegg fraVedlegg(String id) {
        JPASøknadVedlegg vedlegg = new JPASøknadVedlegg();
        vedlegg.setVedleggId(id);
        return vedlegg;
    }

    static List<SøknadInnslag> konverterFra(List<JPASøknadInnslag> innslag) {
        return innslag
                .stream()
                .map(SøknadMapper::tilHistorikkInnslag)
                .collect(toList());
    }
}
