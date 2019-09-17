package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import javax.validation.Valid;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.søknad.SøknadHistorikkController;
import no.nav.security.oidc.api.Unprotected;

@RestController
@Profile({ LOCAL, DEV })
@RequestMapping(path = SøknadHistorikkController.HISTORIKK + "/dev", produces = APPLICATION_JSON_VALUE)
@Unprotected
public class InntektsmeldingDevController {

    private final InntektsmeldingHistorikkTjeneste inntektsmelding;
    private final InntektsmeldingHistorikkHendelseProdusent produsent;

    InntektsmeldingDevController(InntektsmeldingHistorikkTjeneste inntektsmelding,
            InntektsmeldingHistorikkHendelseProdusent produsent) {
        this.inntektsmelding = inntektsmelding;
        this.produsent = produsent;

    }

    @PostMapping("/lagreInntektsmelding")
    public void lagreInntektsmelding(@RequestBody @Valid InntektsmeldingHendelse hendelse) {
        inntektsmelding.lagre(hendelse);
    }

    @PostMapping("/sendInntektsmelding")
    public void sendInntektsmelding(@RequestBody @Valid InntektsmeldingHendelse hendelse) {
        produsent.sendInnsendingHendelse(hendelse);
    }

    @GetMapping("/inntektsmeldinger")
    public List<InntektsmeldingHistorikkInnslag> inntektsmeldinger(@RequestParam("fnr") Fødselsnummer fnr) {
        return inntektsmelding.hentInntektsmeldinger(fnr);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[produsent=" + produsent + ", inntektsmelding=" + inntektsmelding + "]";
    }

}
