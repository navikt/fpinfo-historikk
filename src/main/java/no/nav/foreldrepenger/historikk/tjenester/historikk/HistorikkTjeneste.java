package no.nav.foreldrepenger.historikk.tjenester.historikk;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.historikk.HistorikkMapper.fraEvent;
import static no.nav.foreldrepenger.historikk.tjenester.historikk.HistorikkMapper.konverterFra;
import static no.nav.foreldrepenger.historikk.tjenester.historikk.dao.HistorikkSpec.harFnr;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.historikk.dao.HistorikkRepository;
import no.nav.foreldrepenger.historikk.tjenester.historikk.dao.JPAHistorikkInnslag;
import no.nav.foreldrepenger.historikk.tjenester.innsending.SøknadInnsendingEvent;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogInnslag;
import no.nav.foreldrepenger.historikk.util.TokenUtil;

@Service
@Transactional(JPA_TM)
public class HistorikkTjeneste {

    private static final Sort SORT = new Sort(ASC, "opprettet");
    private static final Logger LOG = LoggerFactory.getLogger(HistorikkTjeneste.class);

    private final HistorikkRepository dao;
    private final TokenUtil tokenUtil;

    public HistorikkTjeneste(HistorikkRepository dao, TokenUtil tokenUtil) {
        this.dao = dao;
        this.tokenUtil = tokenUtil;
    }

    public void lagre(SøknadInnsendingEvent event) {
        if (event != null) {
            LOG.info("Lagrer historikkinnslag fra innsending av {}", event);
            dao.save(fraEvent(event));
            LOG.info("Lagret historikkinnslag OK");
        }
    }

    public void lagre(MinidialogInnslag minidialog, String journalPostId) {
        LOG.info("Lagrer historikkinnslag fra minidialog  {}", minidialog);
        dao.save(fraMinidialog(minidialog, journalPostId));
        LOG.info("Lagret historikkinnslag OK");

    }

    @Transactional(readOnly = true)
    public List<HistorikkInnslag> hentMinHistorikk() {
        return hentHistorikk(tokenUtil.autentisertFNR());
    }

    @Transactional(readOnly = true)
    public List<HistorikkInnslag> hentHistorikk(Fødselsnummer fnr) {
        LOG.info("Hentet historikkinnslag for {}", fnr);
        List<HistorikkInnslag> innslag = konverterFra(dao.findAll(
                where(harFnr(fnr)), SORT));
        LOG.info("Hentet historikkinnslag {}", innslag);
        return innslag;
    }

    private static JPAHistorikkInnslag fraMinidialog(MinidialogInnslag innslag, String journalPostId) {
        JPAHistorikkInnslag historikk = new JPAHistorikkInnslag();
        historikk.setAktørId(innslag.getAktørId());
        historikk.setFnr(innslag.getFnr());
        historikk.setTekst("Spørsmål fra saksbehandler");
        historikk.setSaksnr(innslag.getSaksnr());
        historikk.setJournalpostId(journalPostId);
        return historikk;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [dao=" + dao + ", tokenUtil=" + tokenUtil + "]";
    }

}
