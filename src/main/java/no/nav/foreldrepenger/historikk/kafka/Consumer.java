package no.nav.foreldrepenger.historikk.kafka;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import no.nav.foreldrepenger.historikk.domain.Customer;
import no.nav.foreldrepenger.historikk.repository.CustomerRepository;

@Service
@Profile({ DEV, PREPROD })
public class Consumer {
    private static final Logger LOG = LoggerFactory.getLogger(Producer.class);
    @Autowired
    private CustomerRepository repository;

    @KafkaListener(topics = "#{'${kafka.topic}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void consume(String message) {
        LOG.info(String.format("#### -> Consumed message -> %s", message));
        repository.save(new Customer(message, "Eide"));
        LOG.info("Customers found with findAll():");
        LOG.info("-------------------------------");
        for (Customer customer : repository.findAll()) {
            LOG.info(customer.toString());
        }
    }
}