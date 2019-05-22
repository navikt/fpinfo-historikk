package no.nav.foreldrepenger.historikk.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.security.oidc.api.Unprotected;

@RestController
//@Profile(EnvUtil.DEV)
@RequestMapping(value = "/kafka")
public class KafkaController {
    private final Producer producer;

    @Autowired
    KafkaController(Producer producer) {
        this.producer = producer;
    }

    @GetMapping(value = "/publish")
    @Unprotected
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        this.producer.sendMessage(message);
    }
}
