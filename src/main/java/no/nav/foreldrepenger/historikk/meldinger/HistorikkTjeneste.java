package no.nav.foreldrepenger.historikk.meldinger;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.HistorikkInnslag;
import no.nav.foreldrepenger.historikk.meldinger.dto.JPAHistorikkInnslag;
import no.nav.foreldrepenger.historikk.meldinger.event.InnsendingEvent;

@Service
@Transactional(JPA)
public class HistorikkTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(HistorikkTjeneste.class);

    private final HistorikkRepository dao;
    private final OppslagConnection oppslag;

    public HistorikkTjeneste(HistorikkRepository dao, OppslagConnection oppslag) {
        this.dao = dao;
        this.oppslag = oppslag;
    }

    @Transactional(readOnly = true)
    public List<HistorikkInnslag> hentHistorikkForAktør(AktørId aktørId) {
        return dao.findByAktørId(aktørId.getAktørId())
                .stream()
                .map(HistorikkTjeneste::tilHistorikkInnslag)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<HistorikkInnslag> hentMineMeldinger() {
        return hentHistorikkForAktør(oppslag.hentAktørId());
    }

    private static HistorikkInnslag tilHistorikkInnslag(JPAHistorikkInnslag i) {
        HistorikkInnslag innslag = new HistorikkInnslag(AktørId.valueOf(i.getAktørId()), i.getTekst());
        innslag.setAktiv(i.isAktiv());
        innslag.setDatoMottatt(i.getDatoMottatt());
        innslag.setGyldigTil(i.getGyldigTil());
        innslag.setJournalpostId(i.getJournalpostId());
        innslag.setSaksnr(i.getSaksnr());
        return innslag;

    }

    public void lagre(InnsendingEvent event) {
        if (event != null) {
            LOG.info("Lagrer {}", event);
            dao.save(fraEvent(event));
        }
    }

    private JPAHistorikkInnslag fraEvent(InnsendingEvent event) {

        JPAHistorikkInnslag innslag = new JPAHistorikkInnslag(event.getAktørId(), event.getType().name());
        innslag.setAktiv(true);
        innslag.setGyldigTil(event.getGyldigTil());
        innslag.setSaksnr(event.getSaksNr());
        innslag.setJournalpostId(event.getJournalId());
        innslag.setTekst(event.getType() + " mottatt");
        return innslag;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [dao=" + dao + ", oppslag=" + oppslag + "]";
    }
}
