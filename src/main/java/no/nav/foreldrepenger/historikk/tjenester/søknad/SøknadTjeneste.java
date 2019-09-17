package no.nav.foreldrepenger.historikk.tjenester.søknad;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.søknad.SøknadMapper.fraHendelse;
import static no.nav.foreldrepenger.historikk.tjenester.søknad.SøknadMapper.konverterFra;
import static no.nav.foreldrepenger.historikk.tjenester.søknad.dao.SøknadSpec.harFnr;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.søknad.dao.SøknadRepository;
import no.nav.foreldrepenger.historikk.util.TokenUtil;

@Service
@Transactional(JPA_TM)
public class SøknadTjeneste {

    private static final Sort SORT_OPPRETTET_ASC = new Sort(ASC, "opprettet");
    private static final Logger LOG = LoggerFactory.getLogger(SøknadTjeneste.class);

    private final SøknadRepository dao;
    private final TokenUtil tokenUtil;

    public SøknadTjeneste(SøknadRepository dao, TokenUtil tokenUtil) {
        this.dao = dao;
        this.tokenUtil = tokenUtil;
    }

    public void lagre(SøknadInnsendingHendelse hendelse) {
        LOG.info("Lagrer historikkinnslag fra innsending av {}", hendelse);
        dao.save(fraHendelse(hendelse));
        LOG.info("Lagret historikkinnslag OK");
    }

    @Transactional(readOnly = true)
    public List<SøknadInnslag> søknader() {
        return hentSøknader(tokenUtil.autentisertFNR());
    }

    @Transactional(readOnly = true)
    public List<SøknadInnslag> hentSøknader(Fødselsnummer fnr) {
        LOG.info("Henter søknadshistorikk for {}", fnr);
        List<SøknadInnslag> innslag = konverterFra(dao.findAll(where(harFnr(fnr)), SORT_OPPRETTET_ASC));
        LOG.info("Hentet søknadshistorikk {}", innslag);
        return innslag;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dao=" + dao + ", tokenUtil=" + tokenUtil + "]";
    }
}
