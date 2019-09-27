package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag.SORT_OPPRETTET_ASC;
import static no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingMapper.fraHendelse;
import static no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.dao.JPAInntektsmeldingSpec.harFnr;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.IdempotentTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.dao.JPAInntektsmeldingRepository;
import no.nav.foreldrepenger.historikk.util.TokenUtil;

@Service
@Transactional(JPA_TM)
public class InntektsmeldingTjeneste implements IdempotentTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(InntektsmeldingTjeneste.class);

    private final JPAInntektsmeldingRepository dao;
    private final TokenUtil tokenUtil;

    public InntektsmeldingTjeneste(JPAInntektsmeldingRepository dao,
            TokenUtil tokenUtil) {
        this.dao = dao;
        this.tokenUtil = tokenUtil;
    }

    public void lagre(InntektsmeldingHendelse hendelse) {
        if (skalLagre(hendelse)) {
            LOG.info("Lagrer inntektsmelding fra hendelse {}", hendelse);
            dao.save(fraHendelse(hendelse));
            LOG.info("Lagret inntektsmeldinginnslag OK");
        } else {
            LOG.info("Hendelse med referanseId {} er allerede lagret", hendelse.getReferanseId());
        }
    }

    @Transactional(readOnly = true)
    public List<InntektsmeldingInnslag> inntektsmeldinger() {
        return inntektsmeldinger(tokenUtil.autentisertFNR());
    }

    @Transactional(readOnly = true)
    public List<InntektsmeldingInnslag> inntektsmeldinger(Fødselsnummer fnr) {
        LOG.info("Henter inntektsmeldinghistorikk for {}", fnr);
        List<InntektsmeldingInnslag> innslag = InntektsmeldingMapper.tilInnslag(
                dao.findAll(where(harFnr(fnr)), SORT_OPPRETTET_ASC));
        LOG.info("Hentet inntektsmeldinghistorikk {}", innslag);
        return innslag;
    }

    @Override
    public boolean erAlleredeLagret(String referanseId) {
        return referanseId != null && dao.findByReferanseId(referanseId) == null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dao=" + dao + ", tokenUtil=" + tokenUtil + "]";
    }

}
