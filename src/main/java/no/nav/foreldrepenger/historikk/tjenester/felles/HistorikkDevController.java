package no.nav.foreldrepenger.historikk.tjenester.felles;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.stream.Stream;

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
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingHistorikkTjeneste;
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
    private final SøknadsHistorikkTjeneste søknader;
    private final InntektsmeldingHistorikkTjeneste inntektsmeldinger;

    HistorikkDevController(HistorikkHendelseProdusent produsent, InntektsmeldingHistorikkTjeneste inntektsmeldinger,
            SøknadsHistorikkTjeneste søknader) {
        this.produsent = produsent;
        this.søknader = søknader;
        this.inntektsmeldinger = inntektsmeldinger;

    }

    @PostMapping("/sendSøknad")
    public void produserSøknad(@RequestBody SøknadInnsendingHendelse hendelse) {
        produsent.sendInnsendingHendelse(hendelse);
    }

    @PostMapping("/sendInntektsmelding")
    public void produserInntektsmelding(@RequestBody @Valid InntektsmeldingHendelse hendelse) {
        produsent.sendInnsendingHendelse(hendelse);
    }

    @PostMapping("/lagreInntektsmelding")
    public void lagreInntektsmelding(@RequestBody @Valid InntektsmeldingHendelse hendelse) {
        inntektsmeldinger.lagre(hendelse);
    }

    @GetMapping("/søknader")
    public List<SøknadsHistorikkInnslag> hentSøknader(@RequestParam("fnr") Fødselsnummer fnr) {
        return søknader.hentSøknader(fnr);
    }

    @GetMapping("/inntektsmeldinger")
    public List<InntektsmeldingHistorikkInnslag> hentInntektsmeldinger(@RequestParam("fnr") Fødselsnummer fnr) {
        return inntektsmeldinger.hentInntektsmeldinger(fnr);
    }

    @GetMapping("/historikk")
    public List<? extends HistorikkInnslag> hentHistorikk(@RequestParam("fnr") Fødselsnummer fnr) {
        return Stream.concat(inntektsmeldinger.hentInntektsmeldinger(fnr).stream(), søknader.hentSøknader(fnr).stream())
                .sorted()
                .collect(toList());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[produsent=" + produsent + ", historikk=" + søknader + "]";
    }

}
