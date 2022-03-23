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

    private static final String APPNAVN = "fpinfo-historikk";
    private static final String NAMESPACE = "teamforeldrepenger";
    private static final String OPPGAVE = "O";
    private static final String BESKJED = "B";
    private static final int SIKKERHETSNIVÅ = 3;


    private DittNavMapper() {

    }

    static NokkelInput beskjedNøkkel(Fødselsnummer fnr, String eventId, String grupperingsId) {
        return nøkkelBuilder(fnr, grupperingsId)
            .setEventId(BESKJED + eventId)
            .build();
    }

    static NokkelInput oppgaveNøkkel(Fødselsnummer fnr, String eventId, String grupperingsId) {
        return nøkkelBuilder(fnr, grupperingsId)
            .setEventId(OPPGAVE + eventId)
            .build();
    }

    private static NokkelInput.Builder nøkkelBuilder(Fødselsnummer fnr, String grupperingsId) {
        return NokkelInput.newBuilder()
            .setGrupperingsId(grupperingsId)
            .setFodselsnummer(fnr.getFnr())
            .setAppnavn(APPNAVN)
            .setNamespace(NAMESPACE);
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
