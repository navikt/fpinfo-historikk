package no.nav.foreldrepenger.historikk.kafka;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Melding;
import no.nav.foreldrepenger.historikk.meldingslager.MeldingsLagerTjeneste;
import no.nav.security.oidc.api.Unprotected;

@RestController
@Profile({ DEV, PREPROD })
@RequestMapping(value = "/kafka")
@Unprotected
public class KafkaController {
    private final MeldingsProdusent producer;
    private final MeldingsLagerTjeneste meldingsTjeneste;

    KafkaController(MeldingsProdusent producer, MeldingsLagerTjeneste meldingsTjeneste) {
        this.producer = producer;
        this.meldingsTjeneste = meldingsTjeneste;
    }

    @GetMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("melding") String melding,
            @RequestParam("aktørId") AktørId aktørId) {
        this.producer.sendMelding(new Melding(aktørId, melding));
    }

    @GetMapping("/find")
    public List<Melding> findByFnr(@RequestParam("aktørId") AktørId aktørId) {
        return meldingsTjeneste.hentMeldinger(aktørId);
    }
}
