package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.security.oidc.api.Unprotected;

@RestController
@Profile({ DEV, PREPROD })
@RequestMapping(value = "/innsending/preprod")
@Unprotected
public class InnsendingPreprodController {
    private final InnsendingEventProdusent produsent;

    InnsendingPreprodController(InnsendingEventProdusent produsent) {
        this.produsent = produsent;
    }

    @PostMapping("/produser")
    public void produser(@RequestBody SøknadInnsendingEvent event) {
        produsent.sendInn(event);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [produsent=" + produsent + "]";
    }
}
