package no.nav.foreldrepenger.historikk.meldingslager;

import java.util.List;

import no.nav.foreldrepenger.historikk.meldingslager.dto.JPAMeldingDAO;

public interface MeldingslagerDAO {

    void lagre(JPAMeldingDAO meldingDAO);

    List<JPAMeldingDAO> hentForAktør(String aktørId);

    JPAMeldingDAO hentForId(Integer id);

}
