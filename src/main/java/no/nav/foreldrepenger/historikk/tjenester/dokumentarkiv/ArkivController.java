package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.historikk.http.ProtectedRestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@ProtectedRestController(ArkivController.Arkiv)
@ConditionalOnNotProd
public class ArkivController {
    public static final String Arkiv = "/arkiv";

    private ArkivTjeneste arkiv;

    public ArkivController(ArkivTjeneste arkiv) {
        this.arkiv = arkiv;
    }

    @GetMapping(
        value ="/hent-dokument",
        produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] hent(@RequestParam(name = "dokumentId") String dokumentId) {
        return arkiv.hentDokument(dokumentId);
    }

}
