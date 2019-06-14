package no.nav.foreldrepenger.historikk.meldinger;

import java.util.List;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.meldinger.dto.JPAMinidialogInnslag;

public interface MeldingsLagerDAO {

    void lagre(JPAMinidialogInnslag meldingDAO);

    List<JPAMinidialogInnslag> hentForAktør(String aktørId);

    void markerLest(long id, AktørId aktørId);

}
