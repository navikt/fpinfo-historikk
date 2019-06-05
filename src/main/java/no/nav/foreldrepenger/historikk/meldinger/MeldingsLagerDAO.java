package no.nav.foreldrepenger.historikk.meldinger;

import java.util.List;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMelding;

public interface MeldingsLagerDAO {

    void lagre(JPAMelding meldingDAO);

    List<JPAMelding> hentForAktør(String aktørId);

    void markerLest(long id, AktørId aktørId);

}
