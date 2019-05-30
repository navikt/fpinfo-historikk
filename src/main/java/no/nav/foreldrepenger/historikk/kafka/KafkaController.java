package no.nav.foreldrepenger.historikk.kafka;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final MeldingsProdusent produsent;
    private final MeldingsLagerTjeneste meldingsTjeneste;

    KafkaController(MeldingsProdusent produsent, MeldingsLagerTjeneste meldingsTjeneste) {
        this.produsent = produsent;
        this.meldingsTjeneste = meldingsTjeneste;
    }

    @PostMapping(value = "/publish")
    public ResponseEntity<?> publish(@Valid @RequestBody Melding melding) {
        produsent.sendMelding(melding);
        return created(fromCurrentRequest().path("/{id}").build().toUri()).build();
    }

    @GetMapping("/find")
    public List<Melding> findByAktørId(@RequestParam("aktørId") AktørId aktørId) {
        return meldingsTjeneste.hentMeldingerForAktør(aktørId);
    }

    @GetMapping("/id")
    public Melding findById(@RequestParam("id") Integer id) {
        return meldingsTjeneste.hentMeldingForId(id);
    }
}
