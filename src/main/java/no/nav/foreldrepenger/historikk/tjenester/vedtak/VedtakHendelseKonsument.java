package no.nav.foreldrepenger.historikk.tjenester.vedtak;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.abakus.vedtak.ytelse.Aktør;
import no.nav.abakus.vedtak.ytelse.v1.YtelseV1;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavOperasjoner;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.Oppslag;

@Service
@ConditionalOnProperty(name = "historikk.vedtak.enabled", havingValue = "true")
public class VedtakHendelseKonsument {

    private static final Logger LOG = LoggerFactory.getLogger(VedtakHendelseKonsument.class);

    private final Oppslag oppslag;
    private final DittNavOperasjoner dittNav;
    private final VedtakTjeneste vedtak;

    public VedtakHendelseKonsument(VedtakTjeneste vedtak, DittNavOperasjoner dittNav, Oppslag oppslag) {
        this.dittNav = dittNav;
        this.oppslag = oppslag;
        this.vedtak = vedtak;
    }

    @Transactional
    @KafkaListener(topics = "#{'${historikk.kafka.topics.vedtak}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
    public void behandle(@Payload @Valid YtelseV1 h) {
        LOG.info("Mottok vedtakshendelse {}", h);
        vedtak.lagre(h);
        dittNav.opprettBeskjed(fnr(h.getAktør()), h.getSaksnummer(), "Vedtak " + h.getFagsystem(), "http://www.vg.no",
                h.getVedtakReferanse());
    }

    private Fødselsnummer fnr(Aktør aktør) {
        if (aktør.getVerdi() == null || aktør.getVerdi().isEmpty()) {
            throw new IllegalArgumentException("Aktør ikke satt");
        }
        if (aktør.getVerdi().length() == 11) {
            return Fødselsnummer.valueOf(aktør.getVerdi());
        }
        if (aktør.getVerdi().length() == 13) {
            LOG.info("Slår opp fnr for {}", aktør.getVerdi());
            var fnr = oppslag.fnr(AktørId.valueOf(aktør.getVerdi()));
            LOG.info("Fnr for {} er {}", aktør.getVerdi(), fnr);
            return fnr;
        }
        throw new IllegalArgumentException("Aktør ikke satt");
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dittNav=" + dittNav + "]";
    }

}