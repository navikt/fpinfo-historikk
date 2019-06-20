package no.nav.foreldrepenger.historikk.meldinger;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA;
import static no.nav.foreldrepenger.historikk.meldinger.dao.HistorikkSpec.erEtter;
import static no.nav.foreldrepenger.historikk.meldinger.dao.HistorikkSpec.harAktør;
import static org.springframework.data.jpa.domain.Specification.where;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.HistorikkInnslag;
import no.nav.foreldrepenger.historikk.meldinger.dao.JPAHistorikkInnslag;
import no.nav.foreldrepenger.historikk.meldinger.event.InnsendingEvent;

@Service
@Transactional(JPA)
public class HistorikkTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(HistorikkTjeneste.class);

    private final HistorikkRepository dao;
    private final OppslagTjeneste oppslag;

    public HistorikkTjeneste(HistorikkRepository dao, OppslagTjeneste oppslag) {
        this.dao = dao;
        this.oppslag = oppslag;
    }

    public void lagre(InnsendingEvent event) {
        if (event != null) {
            LOG.info("Lagrer {}", event);
            dao.save(fraEvent(event));
        }
    }

    @Transactional(readOnly = true)
    public List<HistorikkInnslag> hentMinHistorikk() {
        return mapAndCollect(dao.findAll(where(harAktør(oppslag.hentAktørId()))));
    }

    @Transactional(readOnly = true)
    public List<HistorikkInnslag> hentHistorikk(AktørId aktørId) {
        return mapAndCollect(dao.findAll(where(harAktør(aktørId))));
    }

    @Transactional(readOnly = true)
    public List<HistorikkInnslag> hentHistorikkFra(AktørId aktørId, LocalDate dato) {
        return mapAndCollect(dao.findAll(where(harAktør(aktørId)).and(erEtter(dato))));
    }

    private static List<HistorikkInnslag> mapAndCollect(List<JPAHistorikkInnslag> innslag) {
        return innslag
                .stream()
                .map(HistorikkTjeneste::tilHistorikkInnslag)
                .collect(toList());
    }

    private static HistorikkInnslag tilHistorikkInnslag(JPAHistorikkInnslag i) {
        HistorikkInnslag innslag = new HistorikkInnslag(new AktørId(i.getAktørId()), i.getTekst());
        innslag.setDatoMottatt(i.getDatoMottatt());
        innslag.setJournalpostId(i.getJournalpostId());
        innslag.setSaksnr(i.getSaksnr());
        return innslag;

    }

    private JPAHistorikkInnslag fraEvent(InnsendingEvent event) {

        JPAHistorikkInnslag innslag = new JPAHistorikkInnslag(event.getAktørId(), event.getType().name());
        innslag.setSaksnr(event.getSaksNr());
        innslag.setJournalpostId(event.getJournalId());
        innslag.setTekst(event.getType().name());
        return innslag;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [dao=" + dao + ", oppslag=" + oppslag + "]";
    }

}
