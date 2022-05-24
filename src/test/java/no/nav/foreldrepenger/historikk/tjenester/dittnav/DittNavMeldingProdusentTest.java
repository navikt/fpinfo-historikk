package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.SettableListenableFuture;

import no.nav.brukernotifikasjon.schemas.input.BeskjedInput;
import no.nav.brukernotifikasjon.schemas.input.DoneInput;
import no.nav.brukernotifikasjon.schemas.input.NokkelInput;
import no.nav.brukernotifikasjon.schemas.input.OppgaveInput;
import no.nav.foreldrepenger.historikk.config.JpaTxConfiguration;
import no.nav.foreldrepenger.historikk.config.TestJpaTransactionManager;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingHendelse;



@DataJpaTest
@EnableConfigurationProperties({DittNavConfig.class})
@Import(value = {
    DittNavMeldingProdusent.class,
    DittNavMeldingsHistorikk.class,
    JpaTxConfiguration.class,
    TestJpaTransactionManager.class
})
class DittNavMeldingProdusentTest {

    private final Fødselsnummer fnr = Fødselsnummer.valueOf("12345678901");
    private final String referanseId = UUID.randomUUID().toString();
    private final InnsendingHendelse innsendingHendelse = new InnsendingHendelse(
        AktørId.valueOf("001"), fnr, "002", referanseId,
        "003", "004", HendelseType.INITIELL_FORELDREPENGER,
        List.of(), List.of(), LocalDate.now(), LocalDateTime.now());
    private final String DUMMY_TEKST = "Dummy beskjed";

//    @MockBean
//    private OAuth2ClientConfiguration sd;
//    @MockBean
//    private OAuth2HttpClient asdassd;
//    @MockBean
//    private JwtBearerTokenResolver ssaasd;
//    @MockBean
//    private RestTemplateBuilder sasdasdd;

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
        var oppgaveTekst = "Dummy oppgavetekst";

        dittNav.opprettOppgave(innsendingHendelse, oppgaveTekst);
        dittNav.avsluttOppgave(innsendingHendelse.getReferanseId());

        var captor = ArgumentCaptor.forClass(ProducerRecord.class);
        verify(kafkaOperations, times(2)).send((ProducerRecord<NokkelInput, Object>) captor.capture());

        var førsteMelding = captor.getAllValues().get(0);
        assertAll("Første melding skal bestille opprettelse av oppgave",
            () -> assertEquals(førsteMelding.topic(), dittNavConfig.getOppgave()),
            () -> assertThat(førsteMelding.key()).isInstanceOf(NokkelInput.class),
            () -> assertThat(førsteMelding.value()).isInstanceOf(OppgaveInput.class),
            () -> {
                var key = (NokkelInput) førsteMelding.key();
                assertEquals("fpinfo-historikk", key.getAppnavn());
                assertEquals(fnr.getFnr(), key.getFodselsnummer());
                assertEquals(innsendingHendelse.getSaksnummer(), key.getGrupperingsId());
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
                assertEquals(((NokkelInput) førsteMelding.key()).getEventId(), key.getEventId());
                assertEquals("fpinfo-historikk", key.getAppnavn());
                assertEquals(fnr.getFnr(), key.getFodselsnummer());
                assertEquals(innsendingHendelse.getSaksnummer(), key.getGrupperingsId());
            });
    }

    @Test
    public void idempotensSjekk_duplikateHendelserGirÈnOppgaveMeldingPåKø() {
        dittNav.opprettOppgave(innsendingHendelse, DUMMY_TEKST);
        dittNav.opprettOppgave(innsendingHendelse, DUMMY_TEKST);
        var captor = ArgumentCaptor.forClass(ProducerRecord.class);
        verify(kafkaOperations, times(1)).send((ProducerRecord<NokkelInput, Object>) captor.capture());
    }

    @Test
    public void idempotensSjekk_duplikateHendelserGirÈnBeskjedMeldingPåKø() {
        dittNav.opprettBeskjed(innsendingHendelse, DUMMY_TEKST);
        dittNav.opprettBeskjed(innsendingHendelse, DUMMY_TEKST);
        var captor = ArgumentCaptor.forClass(ProducerRecord.class);
        verify(kafkaOperations, times(1)).send((ProducerRecord<NokkelInput, Object>) captor.capture());
    }

    @Test
    public void beskjedOgOppgaveMedSammeReferanseIdGirUlikEventId() {
        var hendelse = new InnsendingHendelse(AktørId.valueOf("001"), fnr, "002", referanseId, "003", "004", HendelseType.INITIELL_FORELDREPENGER,
            List.of(), List.of(), LocalDate.now(), LocalDateTime.now());

        dittNav.opprettOppgave(hendelse, DUMMY_TEKST);
        dittNav.opprettBeskjed(hendelse, DUMMY_TEKST);


        var captor = ArgumentCaptor.forClass(ProducerRecord.class);
        verify(kafkaOperations, times(2)).send((ProducerRecord<NokkelInput, Object>) captor.capture());

        var førsteMelding = captor.getAllValues().get(0);
        assertAll("Oppgave opprettes ok",
            () -> assertEquals(førsteMelding.topic(), dittNavConfig.getOppgave()),
            () -> assertThat(førsteMelding.key()).isInstanceOf(NokkelInput.class),
            () -> assertThat(førsteMelding.value()).isInstanceOf(OppgaveInput.class)
        );
        var oppgaveEksternId = ((NokkelInput) førsteMelding.key()).getEventId();
        assertThat(oppgaveEksternId).isNotEqualTo(hendelse.getReferanseId());

        var andreMelding = captor.getAllValues().get(1);
        assertAll("Beskjed",
            () -> assertEquals(andreMelding.topic(), dittNavConfig.getBeskjed()),
            () -> assertThat(andreMelding.key()).isInstanceOf(NokkelInput.class),
            () -> assertThat(andreMelding.value()).isInstanceOf(BeskjedInput.class),
            () -> {
                var key = (NokkelInput) andreMelding.key();
                assertThat(key.getEventId()).isNotEqualTo(oppgaveEksternId);
                assertEquals("fpinfo-historikk", key.getAppnavn());
                assertEquals(fnr.getFnr(), key.getFodselsnummer());
                assertEquals(hendelse.getSaksnummer(), key.getGrupperingsId());
            },
            () -> {
                var value = (BeskjedInput) andreMelding.value();
                assertThat(value.getTekst()).isEqualTo(DUMMY_TEKST);
            });
    }

}
