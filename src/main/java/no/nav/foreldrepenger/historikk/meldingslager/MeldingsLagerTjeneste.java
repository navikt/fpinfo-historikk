package no.nav.foreldrepenger.historikk.meldingslager;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Melding;
import no.nav.foreldrepenger.historikk.meldingslager.dto.JPAMeldingDAO;

@Service
@Transactional
public class MeldingsLagerTjeneste {

    private static final Logger LOG = LoggerFactory.getLogger(MeldingsLagerTjeneste.class);
    private final MeldingslagerDAO dao;

    public MeldingsLagerTjeneste(MeldingslagerDAO dao) {
        this.dao = dao;
    }

    public void lagre(Melding melding) {
        dao.lagre(new JPAMeldingDAO(melding.getAktørId().getAktørId(), melding.getMelding()));
    }

    @Transactional(readOnly = true)
    public List<Melding> hentMeldingerForAktør(AktørId aktørId) {
        return dao.hentForAktør(aktørId.getAktørId())
                .stream()
                .map(MeldingsLagerTjeneste::tilMelding)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public Melding hentMeldingForId(Integer id) {
        return Optional.ofNullable(dao.hentForId(id))
                .map(MeldingsLagerTjeneste::tilMelding)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Melding> hentAlle() {
        return StreamSupport.stream(dao.hentAlle().spliterator(), false)
                .map(MeldingsLagerTjeneste::tilMelding)
                .collect(toList());
    }

    private static Melding tilMelding(JPAMeldingDAO dao) {
        LOG.trace("Mapper {}", dao);
        Melding melding = new Melding(AktørId.valueOf(dao.getAktørId()), dao.getMelding());
        melding.setDato(dao.getDato());
        return melding;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [dao=" + dao + "]";
    }
}
