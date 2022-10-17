package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.historikk.http.ProtectedRestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@ProtectedRestController(ArkivController.Arkiv)
@ConditionalOnNotProd
public class ArkivController {
    public static final String Arkiv = "/arkiv";

    private ArkivTjeneste arkiv;

    public ArkivController(ArkivTjeneste arkiv) {
        this.arkiv = arkiv;
    }

    @GetMapping("/hent-dokument")
    public byte[] hent(@RequestParam(name = "dokumentId") String dokumentId) {
        return arkiv.hentDokument(dokumentId);
    }

}
