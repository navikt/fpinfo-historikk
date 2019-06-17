package no.nav.foreldrepenger.historikk.meldinger;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMinidialogInnslag;

@Repository
public class RepositoryJPAMeldingsLagerDAO {

    private static final Logger LOG = LoggerFactory.getLogger(RepositoryJPAMeldingsLagerDAO.class);

    private final MinidialogRepository repo;

    public RepositoryJPAMeldingsLagerDAO(MinidialogRepository repo) {
        this.repo = repo;
    }

    public void lagre(JPAMinidialogInnslag meldingDAO) {
        if (meldingDAO.getAktørId().equals("42"))
            throw new IllegalArgumentException("42");
        repo.save(meldingDAO);
    }

    public List<JPAMinidialogInnslag> hentForAktør(String aktørId) {
        LOG.info("Henter meldinger for {}", aktørId);
        return repo.findByAktørId(aktørId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [repo=" + repo + "]";
    }
}
