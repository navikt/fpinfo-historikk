package no.nav.foreldrepenger.historikk.kafka;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.domain.Customer;
import no.nav.foreldrepenger.historikk.repository.CustomerRepository;
import no.nav.security.oidc.api.Unprotected;

@RestController
@Profile({ DEV, PREPROD })
@RequestMapping(value = "/kafka")
public class KafkaController {
    private final Producer producer;
    private static final Logger LOG = LoggerFactory.getLogger(KafkaController.class);
    @Autowired
    private CustomerRepository repository;
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
        int count = jdbcTemplate.queryForObject("SELECT COUNT(FIRST_NAME) FROM CUSTOMER", Integer.class);
        LOG.info("Customers found with findAll():");
        LOG.info("-------------------------------");
        for (Customer customer : repository.findAll()) {
            LOG.info(customer.toString());
        }
        return "OK (" + count + " rows)";
    }
}
