package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.common.util.TokenUtil;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnNotProd
public class ArkivTjeneste {

    private final ArkivConnection connection;
    private final TokenUtil tokenUtil;


    public ArkivTjeneste(ArkivConnection connection, TokenUtil tokenUtil) {
        this.connection = connection;
        this.tokenUtil = tokenUtil;
    }

    public ArkivOppslagJournalposter hentDokumentoversikt() {
        var ident = tokenUtil.getSubject();
        return connection.journalposter(ident);
    }

    public byte[] hentPdf(String journalpostId) {
        var arkiv = connection.journalposter(tokenUtil.getSubject());
        // vi tar første dokument på journalpost
        return arkiv.journalposter().stream()
            .filter(jp -> jp.journalpostId().equals(journalpostId))
            .findFirst()
            .flatMap(o -> o.dokumenter().stream().findFirst())
            .map(ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagDokumentInfo::dokumentInfoId)
            .map(jp -> connection.hentDok(journalpostId, jp))
            .orElse(null);
    }
}
