package no.nav.foreldrepenger.historikk.tjenester.innsending;

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
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingHendelse;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingHistorikkInnslag;
import no.nav.foreldrepenger.historikk.tjenester.søknad.SøknadInnsendingHendelse;
import no.nav.foreldrepenger.historikk.tjenester.søknad.SøknadsHistorikkInnslag;
import no.nav.foreldrepenger.historikk.tjenester.søknad.SøknadsHistorikkTjeneste;
import no.nav.security.oidc.api.Unprotected;

@RestController
@Profile({ LOCAL, DEV })
@RequestMapping(path = HistorikkController.HISTORIKK + "/dev", produces = APPLICATION_JSON_VALUE)
@Unprotected
public class HistorikkDevController {
    private final HistorikkHendelseProdusent produsent;
    private final SøknadsHistorikkTjeneste historikk;

    HistorikkDevController(HistorikkHendelseProdusent produsent, SøknadsHistorikkTjeneste historikk) {
        this.produsent = produsent;
        this.historikk = historikk;
    }

    @PostMapping("/sendSøknad")
    public void produser(@RequestBody SøknadInnsendingHendelse hendelse) {
        produsent.sendInnsendingHendelse(hendelse);
    }

    @PostMapping("/sendInntektsmelding")
    public void produser(@RequestBody @Valid InntektsmeldingHendelse hendelse) {
        produsent.sendInnsendingHendelse(hendelse);
    }

    @GetMapping("/søknader")
    public List<SøknadsHistorikkInnslag> hentSøknader(@RequestParam("fnr") Fødselsnummer fnr) {
        return historikk.hentSøknader(fnr);
    }

    @GetMapping("/inntektsmeldinger")
    public List<InntektsmeldingHistorikkInnslag> hentInntektsmeldinger(@RequestParam("fnr") Fødselsnummer fnr) {
        return historikk.hentInntektsmeldinger(fnr);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[produsent=" + produsent + ", historikk=" + historikk + "]";
    }

}
