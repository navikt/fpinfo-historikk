package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag.SORT_OPPRETTET_ASC;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingMapper.fraHendelse;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingMapper.tilInnslag;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.dao.JPAInnsendingSpec.harFnr;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.innsending.dao.JPAInnsendingRepository;
import no.nav.foreldrepenger.historikk.util.TokenUtil;

@Service
@Transactional(JPA_TM)
public class InnsendingTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(InnsendingTjeneste.class);

    private final JPAInnsendingRepository dao;
    private final TokenUtil tokenUtil;

    public InnsendingTjeneste(JPAInnsendingRepository dao, TokenUtil tokenUtil) {
        this.dao = dao;
        this.tokenUtil = tokenUtil;
    }

    public void lagre(InnsendingHendelse hendelse) {
        if (dao.findByReferanseId(hendelse.getReferanseId()) == null) {
            LOG.info("Lagrer historikkinnslag fra innsending av {}", hendelse);
            dao.save(fraHendelse(hendelse));
            LOG.info("Lagret historikkinnslag OK");
        } else {
            LOG.info("Hendelse med referanseId {} er allerede lagret", hendelse.getReferanseId());
        }

    }

    @Transactional(readOnly = true)
    public List<InnsendingInnslag> søknader() {
        return hentInnsendinger(tokenUtil.autentisertFNR());
    }

    @Transactional(readOnly = true)
    public List<InnsendingInnslag> hentInnsendinger(Fødselsnummer fnr) {
        LOG.info("Henter innsendingsinnslag for {}", fnr);
        List<InnsendingInnslag> innslag = tilInnslag(dao.findAll(where(harFnr(fnr)), SORT_OPPRETTET_ASC));
        LOG.info("Hentet innsendingsinnslag {}", innslag);
        return innslag;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dao=" + dao + ", tokenUtil=" + tokenUtil + "]";
    }
}
