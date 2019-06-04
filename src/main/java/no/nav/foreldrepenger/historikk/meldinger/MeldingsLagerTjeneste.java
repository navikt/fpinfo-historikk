package no.nav.foreldrepenger.historikk.meldinger;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.LeveranseKanal;
import no.nav.foreldrepenger.historikk.domain.Melding;
import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMelding;

@Service
@Transactional
public class MeldingsLagerTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(MeldingsLagerTjeneste.class);

    private final MeldingsLagerDAO dao;
    private final OppslagConnection oppslag;

    @Autowired
    @Value("${fp.test:jalla}")
    String test;

    public MeldingsLagerTjeneste(MeldingsLagerDAO dao, OppslagConnection oppslag) {
        this.dao = dao;
        this.oppslag = oppslag;
    }

    public void lagre(Melding m) {
        dao.lagre(new JPAMelding(m.getAktørId().getAktørId(), m.getMelding(), m.getSaknr(), m.getKanal().name()));
    }

    @Transactional(readOnly = true)
    public List<Melding> hentMeldingerForAktør(AktørId aktørId) {
        return dao.hentForAktør(aktørId.getAktørId())
                .stream()
                .map(MeldingsLagerTjeneste::tilMelding)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<Melding> hentMineMeldinger() {
        AktørId id = oppslag.hentAktørId();
        return hentMeldingerForAktør(id);
    }

    @Transactional(readOnly = true)
    public Melding hentMeldingForId(Long id) {
        return dao.hentForId(id)
                .map(MeldingsLagerTjeneste::tilMelding)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Melding> hentAlle() {
        LOG.info("Henter alle, FPTEST ER " + test);
        return dao.hentAlle()
                .stream()
                .map(MeldingsLagerTjeneste::tilMelding)
                .collect(toList());
    }

    public void merkAlleLest(AktørId aktørId) {
        dao.merkAlle(aktørId.getAktørId());
    }

    private static Melding tilMelding(JPAMelding m) {
        Melding melding = new Melding(AktørId.valueOf(m.getAktørId()), m.getMelding(), m.getSaksnr());
        melding.setDato(m.getDato());
        melding.setKanal(LeveranseKanal.valueOf(m.getKanal()));
        melding.setLest(m.getLest());
        return melding;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [dao=" + dao + ", oppslag=" + oppslag + "]";
    }

}
