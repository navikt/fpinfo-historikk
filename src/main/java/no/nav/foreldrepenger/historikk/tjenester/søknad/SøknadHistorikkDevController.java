package no.nav.foreldrepenger.historikk.tjenester.søknad;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.security.oidc.api.Unprotected;

@RestController
@Profile({ LOCAL, DEV })
@RequestMapping(path = SøknadHistorikkController.HISTORIKK + "/dev", produces = APPLICATION_JSON_VALUE)
@Unprotected
public class SøknadHistorikkDevController {
    private final SøknadHistorikkHendelseProdusent produsent;
    private final SøknadsHistorikkTjeneste søknad;

    SøknadHistorikkDevController(SøknadHistorikkHendelseProdusent produsent,
            SøknadsHistorikkTjeneste søknad) {
        this.produsent = produsent;
        this.søknad = søknad;
    }

    @PostMapping("/sendSøknad")
    public void produserSøknad(@RequestBody SøknadsInnsendingHendelse hendelse) {
        produsent.sendSøknadHendelse(hendelse);
    }

    @GetMapping("/søknader")
    public List<SøknadsHistorikkInnslag> hentSøknader(@RequestParam("fnr") Fødselsnummer fnr) {
        return søknad.hentSøknader(fnr);
    }

    /*
     * @GetMapping("/historikk") public List<? extends HistorikkInnslag>
     * hentHistorikk(@RequestParam("fnr") Fødselsnummer fnr) { return
     * Stream.concat(inntektsmeldinger.hentInntektsmeldinger(fnr).stream(),
     * søknad.hentSøknader(fnr).stream()) .sorted() .collect(toList()); }
     */

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[produsent=" + produsent + ", søknad=" + søknad + "]";
    }

}
