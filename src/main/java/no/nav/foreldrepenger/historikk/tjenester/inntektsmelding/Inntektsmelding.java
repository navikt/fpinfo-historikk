package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag.SORT_OPPRETTET_ASC;
import static no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingMapper.fraHendelse;
import static no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.JPAInntektsmeldingSpec.harAktørId;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.tjenester.felles.IdempotentTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.Oppslag;

@Service
@Transactional(JPA_TM)
public class Inntektsmelding implements IdempotentTjeneste<InntektsmeldingHendelse> {

    private static final Logger LOG = LoggerFactory.getLogger(Inntektsmelding.class);

    private final JPAInntektsmeldingRepository dao;
    private final InntektsmeldingMapper mapper;
    private final Oppslag oppslag;

    public Inntektsmelding(JPAInntektsmeldingRepository dao, InntektsmeldingMapper mapper,
            Oppslag oppslag) {
        this.dao = dao;
        this.mapper = mapper;
        this.oppslag = oppslag;
    }

    @Override
    public boolean opprettOppgave(InntektsmeldingHendelse hendelse) {
        if (!erAlleredeLagret(hendelse.getReferanseId())) {
            LOG.info("Lagrer inntektsmeldinginnslag fra hendelse {}", hendelse);
            dao.save(fraHendelse(hendelse));
            LOG.info("Lagret inntektsmeldinginnslag OK");
            return true;
        } else {
            LOG.info("Hendelse med referanseId {} er allerede lagret", hendelse.getReferanseId());
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<InntektsmeldingInnslag> inntektsmeldinger() {
        return inntektsmeldinger(oppslag.aktørId());
    }

    @Transactional(readOnly = true)
    public List<InntektsmeldingInnslag> inntektsmeldinger(AktørId id) {
        LOG.info("Henter inntektsmeldinghistorikk for {}", id);
        List<InntektsmeldingInnslag> innslag = mapper
                .tilInnslag(dao.findAll(where(harAktørId(id)), SORT_OPPRETTET_ASC));
        LOG.info("Hentet inntektsmeldinghistorikk {}", innslag);
        return innslag;
    }

    @Override
    public boolean erAlleredeLagret(String referanseId) {
        return referanseId != null && dao.findByReferanseId(referanseId) != null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dao=" + dao + ", mapper=" + mapper + ", oppslag=" + oppslag + "]";
    }

}
