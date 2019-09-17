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
@RequestMapping(path = SøknadController.HISTORIKK + "/dev", produces = APPLICATION_JSON_VALUE)
@Unprotected
public class SøknadDevController {
    private final SøknadHendelseProdusent produsent;
    private final SøknadTjeneste søknad;

    SøknadDevController(SøknadHendelseProdusent produsent,
            SøknadTjeneste søknad) {
        this.produsent = produsent;
        this.søknad = søknad;
    }

    @PostMapping("/sendSøknad")
    public void produserSøknad(@RequestBody SøknadInnsendingHendelse hendelse) {
        produsent.sendSøknadHendelse(hendelse);
    }

    @PostMapping("/lagreSøknad")
    public void lagreSøknad(@RequestBody SøknadInnsendingHendelse hendelse) {
        søknad.lagre(hendelse);
    }

    @GetMapping("/søknader")
    public List<SøknadInnslag> hentSøknader(@RequestParam("fnr") Fødselsnummer fnr) {
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
