package no.nav.foreldrepenger.historikk.meldinger;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import java.util.List;

import javax.validation.Valid;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Melding;
import no.nav.security.oidc.api.Unprotected;

@RestController
@Profile({ DEV, PREPROD })
@RequestMapping(value = "/historikk/preprod")
@Unprotected
@Api(value = "Endpoint for message management", protocols = "http,https")
public class MeldingPreprodController {
    private final MeldingProdusent produsent;
    private final MeldingsLagerTjeneste meldingsTjeneste;

    MeldingPreprodController(MeldingProdusent produsent, MeldingsLagerTjeneste meldingsTjeneste) {
        this.produsent = produsent;
        this.meldingsTjeneste = meldingsTjeneste;
    }

    @PostMapping(value = "/publish")
    public void sendMelding(@Valid @RequestBody Melding melding) {
        produsent.sendMelding(melding);
    }

    @PostMapping(value = "/send")
    public void sendMelding(@Valid @RequestBody String melding) {
        produsent.sendSøknad(melding);
    }

    @GetMapping("/find")
    public List<Melding> hentMeldingerForAktør(@RequestParam("aktørId") AktørId aktørId) {
        return meldingsTjeneste.hentMeldingerForAktør(aktørId);
    }

    @GetMapping("/merk")
    public void markerLest(@RequestParam("id") long id) {
        meldingsTjeneste.markerLest(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [produsent=" + produsent + ", meldingsTjeneste=" + meldingsTjeneste + "]";
    }
}
