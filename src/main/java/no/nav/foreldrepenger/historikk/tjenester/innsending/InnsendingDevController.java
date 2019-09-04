package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;

import javax.validation.Valid;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingHendelse;
import no.nav.foreldrepenger.historikk.tjenester.søknad.SøknadInnsendingHendelse;
import no.nav.security.oidc.api.Unprotected;

@RestController
@Profile({ LOCAL, DEV })
@RequestMapping(value = "/innsending/dev")
@Unprotected
public class InnsendingDevController {
    private final InnsendingHendelseProdusent innsending;

    InnsendingDevController(InnsendingHendelseProdusent innsending) {
        this.innsending = innsending;
    }

    @PostMapping("/sendInn")
    public void produser(@RequestBody SøknadInnsendingHendelse hendelse) {
        innsending.sendInnsendingHendelse(hendelse);
    }

    @PostMapping("/inntektsmelding")
    public void produser(@RequestBody @Valid InntektsmeldingHendelse hendelse) {
        innsending.sendInnsendingHendelse(hendelse);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [innsending=" + innsending + "]";
    }
}
