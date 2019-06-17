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

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.Api;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.HistorikkInnslag;
import no.nav.foreldrepenger.historikk.domain.MinidialogInnslag;
import no.nav.foreldrepenger.historikk.meldinger.event.InnsendingEvent;
import no.nav.security.oidc.api.Unprotected;

@RestController
@Profile({ DEV, PREPROD })
@RequestMapping(value = "/historikk/preprod")
@Unprotected
@Api(value = "Endpoint for message management", protocols = "http,https")
public class MeldingPreprodController {
    private final MeldingProdusent produsent;
    private final MeldingsLagerTjeneste meldingsTjeneste;
    private final HistorikkTjeneste historikk;

    MeldingPreprodController(MeldingProdusent produsent, MeldingsLagerTjeneste meldingsTjeneste,
            HistorikkTjeneste historikk) {
        this.produsent = produsent;
        this.meldingsTjeneste = meldingsTjeneste;
        this.historikk = historikk;
    }

    @PostMapping(value = "/publish")
    public void sendMelding(@Valid @RequestBody MinidialogInnslag melding) throws JsonProcessingException {
        produsent.sendMelding(melding);
    }

    @PostMapping(value = "/send")
    public void sendMelding(@Valid @RequestBody String melding) {
        produsent.sendSøknad(melding);
    }

    @GetMapping("/find")
    public List<MinidialogInnslag> hentMeldingerForAktør(@RequestParam("aktørId") AktørId aktørId) {
        return meldingsTjeneste.hentMeldingerForAktør(aktørId);
    }

    @GetMapping("/historikk")
    public List<HistorikkInnslag> hentHistorikkForAktør(@RequestParam("aktørId") AktørId aktørId) {
        return historikk.hentHistorikkForAktør(aktørId);
    }

    @PostMapping("/historikk/lagre")
    public void lagreHistorikk(InnsendingEvent event) {
        historikk.lagre(event);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [produsent=" + produsent + ", meldingsTjeneste=" + meldingsTjeneste + "]";
    }
}
