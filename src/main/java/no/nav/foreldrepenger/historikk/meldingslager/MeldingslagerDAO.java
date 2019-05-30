package no.nav.foreldrepenger.historikk.meldingslager;

import java.util.List;

import no.nav.foreldrepenger.historikk.meldingslager.dto.MeldingDAO;

public interface MeldingslagerDAO {

    void lagre(MeldingDAO meldingDAO);

    List<MeldingDAO> hent(String aktørId);

}
