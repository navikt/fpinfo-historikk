package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import no.nav.brukernotifikasjon.schemas.input.DoneInput;
import no.nav.brukernotifikasjon.schemas.input.NokkelInput;
import no.nav.brukernotifikasjon.schemas.input.OppgaveInput;
import no.nav.foreldrepenger.historikk.FPInfoHistorikkApplicationLocal;
import no.nav.foreldrepenger.historikk.config.JpaTxConfiguration;
import no.nav.foreldrepenger.historikk.config.TestJpaTransactionManager;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingHendelse;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {
    DittNavMeldingProdusent.class,
    DittNavMeldingsHistorikk.class,
    JpaTxConfiguration.class,
    TestJpaTransactionManager.class,
    FPInfoHistorikkApplicationLocal.class})
@TestPropertySource(locations = "dittnav.properties")
@EnableConfigurationProperties({DittNavConfig.class})
@DataJpaTest
class DittNavMeldingProdusentTest {

    private final Fødselsnummer fnr = Fødselsnummer.valueOf("12345678901");
    private final String referanseId = UUID.randomUUID().toString();

    @MockBean
    private KafkaOperations<NokkelInput, Object> kafkaOperations;
    @Autowired
    private DittNavConfig dittNavConfig;
    @Autowired
    private DittNavMeldingProdusent dittNav;


    @BeforeEach
    public void setUp() {
        var future = new SettableListenableFuture<SendResult<NokkelInput, Object>>();
        future.setException(new IllegalStateException("Ooops"));
        when(kafkaOperations.send((ProducerRecord<NokkelInput, Object>) any())).thenReturn(future);
    }

    @Test
    public void oppgaveLifecycle() {
        var hendelse = new InnsendingHendelse(AktørId.valueOf("001"), fnr, "002", referanseId, "003", "004", HendelseType.INITIELL_FORELDREPENGER,
            List.of(), List.of(), LocalDate.now(), LocalDateTime.now());
        var oppgaveTekst = "Dummy oppgavetekst";

        dittNav.opprettOppgave(hendelse, oppgaveTekst);
        dittNav.avsluttOppgave(fnr, hendelse.getSaksnummer(), hendelse.getReferanseId());

        var captor = ArgumentCaptor.forClass(ProducerRecord.class);
        verify(kafkaOperations, times(2)).send((ProducerRecord<NokkelInput, Object>) captor.capture());

        var førsteMelding = captor.getAllValues().get(0);
        assertAll("Første melding skal bestille opprettelse av oppgave",
            () -> assertEquals(førsteMelding.topic(), dittNavConfig.getOppgave()),
            () -> assertThat(førsteMelding.key()).isInstanceOf(NokkelInput.class),
            () -> assertThat(førsteMelding.value()).isInstanceOf(OppgaveInput.class),
            () -> {
                var key = (NokkelInput) førsteMelding.key();
                assertEquals(hendelse.getReferanseId(), key.getEventId());
                assertEquals("fpinfo-historikk", key.getAppnavn());
                assertEquals(fnr.getFnr(), key.getFodselsnummer());
                assertEquals(hendelse.getSaksnummer(), key.getGrupperingsId());
            },
            () -> {
                var value = (OppgaveInput) førsteMelding.value();
                assertEquals(value.getTekst(), oppgaveTekst);
            });

        var andreMelding = captor.getAllValues().get(1);
        assertAll("Andre melding skal bestille lukking av oppgave",
            () -> assertEquals(andreMelding.topic(), dittNavConfig.getDone()),
            () -> assertThat(andreMelding.key()).isInstanceOf(NokkelInput.class),
            () -> assertThat(andreMelding.value()).isInstanceOf(DoneInput.class),
            () -> {
                var key = (NokkelInput) andreMelding.key();
                assertEquals(hendelse.getReferanseId(), key.getEventId());
                assertEquals("fpinfo-historikk", key.getAppnavn());
                assertEquals(fnr.getFnr(), key.getFodselsnummer());
                assertEquals(hendelse.getSaksnummer(), key.getGrupperingsId());
            });
    }

}
