package no.nav.foreldrepenger.historikk.meldinger;

import java.util.List;

import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMelding;

public interface MeldingsLagerDAO {

    void lagre(JPAMelding meldingDAO);

    List<JPAMelding> hentForAktør(String aktørId);

    void markerLest(String aktørId);

}
