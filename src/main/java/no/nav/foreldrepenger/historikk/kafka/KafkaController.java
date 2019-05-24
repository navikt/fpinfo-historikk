package no.nav.foreldrepenger.historikk.kafka;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.domain.Melding;
import no.nav.foreldrepenger.historikk.repository.MeldingRepository;
import no.nav.security.oidc.api.Unprotected;

@RestController
@Profile({ DEV, PREPROD })
@RequestMapping(value = "/kafka")
@Unprotected
public class KafkaController {
    private final Producer producer;
    @Autowired
    private MeldingRepository repository;

    @Autowired
    KafkaController(Producer producer) {
        this.producer = producer;
    }

    @GetMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("melding") String melding, @RequestParam("fnr") String fnr) {
        this.producer.sendMelding(new Melding(fnr, melding));
    }

    @GetMapping("/find")
    public List<Melding> findByFnr(@RequestParam("fnr") String fnr) {
        return repository.findByFnr(fnr);
    }
}
