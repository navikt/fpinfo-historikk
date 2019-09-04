package no.nav.foreldrepenger.historikk.tjenester.søknad;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.søknad.SøknadsHistorikkMapper.fraHendelse;
import static no.nav.foreldrepenger.historikk.tjenester.søknad.SøknadsHistorikkMapper.fraMinidialog;
import static no.nav.foreldrepenger.historikk.tjenester.søknad.SøknadsHistorikkMapper.konverterFra;
import static no.nav.foreldrepenger.historikk.tjenester.søknad.dao.SøknadsHistorikkSpec.harFnr;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogHendelse;
import no.nav.foreldrepenger.historikk.tjenester.søknad.dao.SøknadsHistorikkRepository;
import no.nav.foreldrepenger.historikk.util.TokenUtil;

@Service
@Transactional(JPA_TM)
public class SøknadsHistorikkTjeneste {

    private static final Sort SORT_OPPRETTET_ASC = new Sort(ASC, "opprettet");
    private static final Logger LOG = LoggerFactory.getLogger(SøknadsHistorikkTjeneste.class);

    private final SøknadsHistorikkRepository søknadDao;
    private final TokenUtil tokenUtil;

    public SøknadsHistorikkTjeneste(SøknadsHistorikkRepository søknadDao,
            TokenUtil tokenUtil) {
        this.søknadDao = søknadDao;
        this.tokenUtil = tokenUtil;
    }

    public void lagre(SøknadInnsendingHendelse hendelse) {
        LOG.info("Lagrer historikkinnslag fra innsending av {}", hendelse);
        søknadDao.save(fraHendelse(hendelse));
        LOG.info("Lagret historikkinnslag OK");
    }

    public void lagre(MinidialogHendelse minidialog, String journalPostId) {
        LOG.info("Lagrer historikkinnslag fra minidialog {}", minidialog);
        søknadDao.save(fraMinidialog(minidialog, journalPostId));
        LOG.info("Lagret historikkinnslag OK");
    }

    @Transactional(readOnly = true)
    public List<SøknadsHistorikkInnslag> hentMinHistorikk() {
        return hentSøknader(tokenUtil.autentisertFNR());
    }

    @Transactional(readOnly = true)
    public List<SøknadsHistorikkInnslag> hentSøknader(Fødselsnummer fnr) {
        LOG.info("Henter søknadshistorikk for {}", fnr);
        List<SøknadsHistorikkInnslag> innslag = konverterFra(søknadDao.findAll(where(harFnr(fnr)), SORT_OPPRETTET_ASC));
        LOG.info("Hentet søknadshistorikk {}", innslag);
        return innslag;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[søknadDao=" + søknadDao
                + ", tokenUtil=" + tokenUtil + "]";
    }

}
