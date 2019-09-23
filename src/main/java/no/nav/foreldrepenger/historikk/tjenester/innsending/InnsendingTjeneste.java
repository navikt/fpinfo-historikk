package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag.SORT_OPPRETTET_ASC;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingMapper.fraHendelse;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingMapper.konverterFra;
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

    public void lagre(InnsendingInnsendingHendelse hendelse) {
        LOG.info("Lagrer historikkinnslag fra innsending av {}", hendelse);
        dao.save(fraHendelse(hendelse));
        LOG.info("Lagret historikkinnslag OK");
    }

    @Transactional(readOnly = true)
    public List<InnsendingInnslag> søknader() {
        return hentSøknader(tokenUtil.autentisertFNR());
    }

    @Transactional(readOnly = true)
    public List<InnsendingInnslag> hentSøknader(Fødselsnummer fnr) {
        LOG.info("Henter søknadshistorikk for {}", fnr);
        List<InnsendingInnslag> innslag = konverterFra(dao.findAll(where(harFnr(fnr)), SORT_OPPRETTET_ASC));
        LOG.info("Hentet søknadshistorikk {}", innslag);
        return innslag;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dao=" + dao + ", tokenUtil=" + tokenUtil + "]";
    }
}
