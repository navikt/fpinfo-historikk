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
@RequestMapping(value = "/historikk")
@Unprotected
@Api(value = "Endpoint for message management", protocols = "http,https")
public class MeldingController {
    private final MeldingProdusent produsent;
    private final MeldingsLagerTjeneste meldingsTjeneste;

    MeldingController(MeldingProdusent produsent, MeldingsLagerTjeneste meldingsTjeneste) {
        this.produsent = produsent;
        this.meldingsTjeneste = meldingsTjeneste;
    }

    @PostMapping(value = "/publish")
    public void sendMelding(@Valid @RequestBody Melding melding) {
        produsent.sendMelding(melding);
    }

    @GetMapping("/find")
    public List<Melding> hentMeldingerForAktør(@RequestParam("aktørId") AktørId aktørId) {
        return meldingsTjeneste.hentMeldingerForAktør(aktørId);
    }

    @GetMapping("/find/me")
    public List<Melding> hentMeldingerForAktør() {
        return meldingsTjeneste.hentMeldingerForAktør();
    }

    @GetMapping("/merk")
    public void merkAlleLestForAktør(@RequestParam("aktørId") AktørId aktørId) {
        meldingsTjeneste.merkAlleLest(aktørId);
    }

    @GetMapping("/alle")
    public List<Melding> hentAlle() {
        return meldingsTjeneste.hentAlle();
    }

    @GetMapping("/id")
    public Melding hentMeldingForId(@RequestParam("id") Long id) {
        return meldingsTjeneste.hentMeldingForId(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [produsent=" + produsent + ", meldingsTjeneste=" + meldingsTjeneste + "]";
    }
}
