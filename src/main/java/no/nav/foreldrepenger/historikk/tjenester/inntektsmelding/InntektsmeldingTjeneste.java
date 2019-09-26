package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag.SORT_OPPRETTET_ASC;
import static no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingMapper.fraInntektsmeldingHendelse;
import static no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.dao.JPAInntektsmeldingSpec.harFnr;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.dao.JPAInntektsmeldingRepository;
import no.nav.foreldrepenger.historikk.util.TokenUtil;

@Service
@Transactional(JPA_TM)
public class InntektsmeldingTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(InntektsmeldingTjeneste.class);

    private final JPAInntektsmeldingRepository dao;
    private final TokenUtil tokenUtil;

    public InntektsmeldingTjeneste(JPAInntektsmeldingRepository dao,
            TokenUtil tokenUtil) {
        this.dao = dao;
        this.tokenUtil = tokenUtil;
    }

    public void lagre(InntektsmeldingHendelse hendelse) {
        if (dao.findByReferanseId(hendelse.getReferanseId()) == null) {
            LOG.info("Lagret inntektsmelding OK");
            dao.save(fraInntektsmeldingHendelse(hendelse));
            LOG.info("Lagret inntektsmeldinginnslag OK");
        } else {
            LOG.info("Hendelse med referanseId {} er allerede lagret", hendelse.getReferanseId());
        }
    }

    @Transactional(readOnly = true)
    public List<InntektsmeldingInnslag> inntektsmeldinger() {
        return hentInntektsmeldinger(tokenUtil.autentisertFNR());
    }

    @Transactional(readOnly = true)
    public List<InntektsmeldingInnslag> hentInntektsmeldinger(Fødselsnummer fnr) {
        LOG.info("Henter inntektsmeldinghistorikk for {}", fnr);
        List<InntektsmeldingInnslag> innslag = InntektsmeldingMapper.konverterFra(
                dao.findAll(where(harFnr(fnr)), SORT_OPPRETTET_ASC));
        LOG.info("Hentet inntektsmeldinghistorikk {}", innslag);
        return innslag;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dao=" + dao + ", tokenUtil=" + tokenUtil + "]";
    }

}
