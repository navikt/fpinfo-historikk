package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import no.nav.foreldrepenger.historikk.http.ProtectedRestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ProtectedRestController(ArkivController.Arkiv)
public class ArkivController {
    public static final String Arkiv = "/arkiv";

    private final ArkivTjeneste arkiv;

    public ArkivController(ArkivTjeneste arkiv) {
        this.arkiv = arkiv;
    }

    @GetMapping(
        value ="/hent-dokument/{journalpostId}/{dokumentId}",
        produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] hent(@PathVariable(name = "journalpostId") String journalpostId,
                                     @PathVariable(name = "dokumentId") String dokumentId) {
        return arkiv.hentPdf(journalpostId, dokumentId);
    }

    @GetMapping(
        value = "/alle",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<ArkivDokument> hentDokumentoversikt() {
        return arkiv.hentDokumentoversikt();
    }

}
