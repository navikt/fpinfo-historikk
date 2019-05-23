package no.nav.foreldrepenger.historikk.kafka;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.security.oidc.api.Unprotected;

@RestController
@Profile({ DEV, PREPROD })
@RequestMapping(value = "/kafka")
public class KafkaController {
    private final Producer producer;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    KafkaController(Producer producer) {
        this.producer = producer;
    }

    @GetMapping(value = "/publish")
    @Unprotected
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        this.producer.sendMessage(message);
    }

    @GetMapping("/count")
    @Unprotected
    public String ready() {
        int count = jdbcTemplate.queryForObject("SELECT COUNT(FIRSTNAME) FROM CUSTOMER1", Integer.class);
        return "OK (" + count + " rows)";
    }
}
