package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;

import no.nav.brukernotifikasjon.schemas.input.BeskjedInput;
import no.nav.brukernotifikasjon.schemas.input.DoneInput;
import no.nav.brukernotifikasjon.schemas.input.NokkelInput;
import no.nav.brukernotifikasjon.schemas.input.OppgaveInput;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

final class DittNavMapper {

    static final String OPPGAVE = "O";

    static final String BESKJED = "B";

    private static final int SIKKERHETSNIVÅ = 3;

    private DittNavMapper() {

    }

    static NokkelInput nøkkel(Fødselsnummer fnr, String eventId, String grupperingsId) {
        return NokkelInput.newBuilder()
            .setEventId(eventId)
            .setGrupperingsId(grupperingsId)
            .setFodselsnummer(fnr.getFnr())
            .setAppnavn("fpinfo-historikk")
            .setNamespace("teamforeldrepenger")
            .build();
    }

    static DoneInput avslutt() {
        return DoneInput.newBuilder()
            .setTidspunkt(Instant.now().toEpochMilli())
            .build();
    }

    static OppgaveInput oppgaveInput(String tekst, URI landingsside) {
        return OppgaveInput.newBuilder()
                .setLink(landingsside.toString())
                .setSikkerhetsnivaa(SIKKERHETSNIVÅ)
                .setTekst(tekst)
                .setTidspunkt(Instant.now().toEpochMilli()).build();
    }

    static BeskjedInput beskjedInput(String tekst, URI landingsside, Duration duration) {
        return BeskjedInput.newBuilder()
            .setSynligFremTil(Instant.now().plus(duration).toEpochMilli())
            .setLink(landingsside.toString())
            .setSikkerhetsnivaa(SIKKERHETSNIVÅ)
            .setTekst(tekst)
            .setTidspunkt(Instant.now().toEpochMilli()).build();
    }

}
