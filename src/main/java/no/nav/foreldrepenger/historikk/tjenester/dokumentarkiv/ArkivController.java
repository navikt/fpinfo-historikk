package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.security.token.support.spring.ProtectedRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivController.Arkiv;

//@ProtectedRestController(ArkivController.Arkiv)
@ProtectedRestController(value={ Arkiv }, issuer="tokenx")
@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
@ConditionalOnNotProd
public class ArkivController {
    public static final String Arkiv = "/arkiv";

    private ArkivTjeneste arkiv;

    public ArkivController(ArkivTjeneste arkiv) {
        this.arkiv = arkiv;
    }

    @GetMapping(
        value ="/hent-dokument/{journalpostId}/{dokumentId}",
        produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] hent(@PathVariable(name = "journalpostId") String journalpostId, @RequestParam(name = "dokumentId") String dokumentId) {
        return arkiv.hentPdf(journalpostId, dokumentId);
    }

    @GetMapping(
        value = "/alle",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<ArkivDokument> hentDokumentoversikt() {
        return arkiv.hentDokumentoversikt();
    }

}
