package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import no.nav.foreldrepenger.common.util.TokenUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArkivTjeneste {

    private final ArkivConnection connection;
    private final TokenUtil tokenUtil;


    public ArkivTjeneste(ArkivConnection connection, TokenUtil tokenUtil) {
        this.connection = connection;
        this.tokenUtil = tokenUtil;
    }

    public List<ArkivDokument> hentDokumentoversikt() {
        var ident = tokenUtil.getSubject();
        return connection.journalposter(ident);
    }

    public byte[] hentPdf(String journalpostId, String dokumentId) {
        return connection.hentDok(journalpostId, dokumentId);
    }
}
