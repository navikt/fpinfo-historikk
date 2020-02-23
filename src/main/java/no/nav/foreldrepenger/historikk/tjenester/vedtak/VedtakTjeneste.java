package no.nav.foreldrepenger.historikk.tjenester.vedtak;

import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag.SORT_OPPRETTET_ASC;
import static no.nav.foreldrepenger.historikk.tjenester.vedtak.JPAVedtakSpec.erGyldig;
import static no.nav.foreldrepenger.historikk.tjenester.vedtak.JPAVedtakSpec.harFnr;
import static no.nav.foreldrepenger.historikk.tjenester.vedtak.VedtakMapper.tilVedtak;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.abakus.vedtak.ytelse.v1.YtelseV1;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.Oppslag;
import no.nav.foreldrepenger.historikk.util.TokenUtil;

@Service
@Transactional(JPA_TM)
public class VedtakTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(VedtakTjeneste.class);

    private final JPAVedtakRepository dao;
    private final Oppslag oppslag;
    private final TokenUtil tokenUtil;

    public VedtakTjeneste(JPAVedtakRepository dao, Oppslag oppslag, TokenUtil tokenUtil) {
        this.dao = dao;
        this.oppslag = oppslag;
        this.tokenUtil = tokenUtil;
    }

    public boolean lagre(YtelseV1 h) {
        var orig = dao.findByVedtakReferanse(h.getVedtakReferanse());
        if (orig == null) {
            LOG.info("Lagrer vedtak {}", h);
            // dao.save(VedtakMapper.fraVedtak(h));
            LOG.info("Lagret vedtak OK");
            return true;
        }
        LOG.info("Vedtak med referanseId {} er allerede lagret", h.getVedtakReferanse());
        return false;
    }

    @Transactional(readOnly = true)
    public List<VedtakInnslag> vedtak() {
        return vedtak(tokenUtil.autentisertFNR());
    }

    @Transactional(readOnly = true)
    public List<VedtakInnslag> vedtak(Fødselsnummer fnr) {
        LOG.info("Henter vedtaksinnslag for {}", fnr);
        return tilVedtak(dao.findAll(where(harFnr(fnr).and(erGyldig())), SORT_OPPRETTET_ASC));
    }

    public boolean erAlleredeLagret(String referanseId) {
        return referanseId != null && dao.findByVedtakReferanse(referanseId) != null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dao=" + dao + ", oppslag=" + oppslag + ", tokenUtil=" + tokenUtil + "]";
    }
}
