package no.nav.foreldrepenger.historikk.meldinger;

import java.util.List;
import java.util.Optional;

import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMelding;

public interface MeldingsLagerDAO {

    void lagre(JPAMelding meldingDAO);

    List<JPAMelding> hentForAktør(String aktørId);

    Optional<JPAMelding> hentForId(Long id);

    List<JPAMelding> hentAlle();

    void merkAlle(String aktørId);

}
