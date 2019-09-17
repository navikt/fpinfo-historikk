package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.dao.InntektsmeldingHistorikkSpec.harFnr;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.dao.InntektsmeldingRepository;
import no.nav.foreldrepenger.historikk.util.TokenUtil;

@Service
@Transactional(JPA_TM)
public class InntektsmeldingTjeneste {

    private static final Sort SORT_OPPRETTET_ASC = new Sort(ASC, "opprettet");
    private static final Logger LOG = LoggerFactory.getLogger(InntektsmeldingTjeneste.class);

    private final InntektsmeldingRepository inntektsmeldingDao;
    private final TokenUtil tokenUtil;

    public InntektsmeldingTjeneste(InntektsmeldingRepository inntektsmeldingDao,
            TokenUtil tokenUtil) {
        this.inntektsmeldingDao = inntektsmeldingDao;
        this.tokenUtil = tokenUtil;
    }

    public void lagre(InntektsmeldingHendelse hendelse) {
        LOG.info("Lagrer inntektsmelding fra innsending av {}", hendelse);
        inntektsmeldingDao.save(InntektsmeldingMapper.fraInntektsmeldingHendelse(hendelse));
        LOG.info("Lagret inntektsmelding OK");
    }

    @Transactional(readOnly = true)
    public List<InntektsmeldingInnslag> inntektsmeldinger() {
        return hentInntektsmeldinger(tokenUtil.autentisertFNR());
    }

    @Transactional(readOnly = true)
    public List<InntektsmeldingInnslag> hentInntektsmeldinger(Fødselsnummer fnr) {
        LOG.info("Henter inntektsmeldinghistorikk for {}", fnr);
        List<InntektsmeldingInnslag> innslag = InntektsmeldingMapper.konverterFra(
                inntektsmeldingDao.findAll(where(harFnr(fnr)), SORT_OPPRETTET_ASC));
        LOG.info("Hentet inntektsmeldinghistorikk {}", innslag);
        return innslag;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[inntektsmeldingDao=" + inntektsmeldingDao + ", tokenUtil=" + tokenUtil
                + "]";
    }

}
