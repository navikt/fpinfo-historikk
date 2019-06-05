package no.nav.foreldrepenger.historikk.meldinger;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import no.nav.foreldrepenger.historikk.domain.Melding;
import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@RequestMapping(value = "/historikk")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
@Api(value = "Endpoint for message management", protocols = "http,https")
public class MeldingController {
    private final MeldingProdusent produsent;
    private final MeldingsLagerTjeneste meldingsTjeneste;

    MeldingController(MeldingProdusent produsent, MeldingsLagerTjeneste meldingsTjeneste) {
        this.produsent = produsent;
        this.meldingsTjeneste = meldingsTjeneste;
    }

    @GetMapping("/find/me")
    public List<Melding> meldinger() {
        return meldingsTjeneste.hentMineMeldinger();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [produsent=" + produsent + ", meldingsTjeneste=" + meldingsTjeneste + "]";
    }
}
