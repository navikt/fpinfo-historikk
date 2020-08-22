package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import java.time.Duration;
import java.time.Instant;

import no.nav.brukernotifikasjon.schemas.Beskjed;
import no.nav.brukernotifikasjon.schemas.Done;
import no.nav.brukernotifikasjon.schemas.Nokkel;
import no.nav.brukernotifikasjon.schemas.Oppgave;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

final class DittNavMapper {

    private static final String OPPGAVE = "O";

    private static final String BESKJED = "B";

    private static final String SYSTEMBRUKER = "srvfpinfo-historikk";

    private static final int SIKKERHETSNIVÅ = 3;

    private DittNavMapper() {

    }

    static Oppgave oppgave(Fødselsnummer fnr, String grupperingsId, String tekst, String url) {
        return Oppgave.newBuilder()
                .setFodselsnummer(fnr.getFnr())
                .setGrupperingsId(grupperingsId)
                .setLink(url)
                .setSikkerhetsnivaa(SIKKERHETSNIVÅ)
                .setTekst(tekst)
                .setTidspunkt(Instant.now().toEpochMilli()).build();
    }

    static Done avslutt(Fødselsnummer fnr, String grupperingsId) {
        return Done.newBuilder()
                .setFodselsnummer(fnr.getFnr())
                .setGrupperingsId(grupperingsId)
                .setTidspunkt(Instant.now().toEpochMilli()).build();
    }

    static Beskjed beskjed(Fødselsnummer fnr, String grupperingsId, String tekst, String url, Duration duration) {
        return Beskjed.newBuilder()
                .setSynligFremTil(Instant.now().plus(duration).toEpochMilli())
                .setFodselsnummer(fnr.getFnr())
                .setGrupperingsId(grupperingsId)
                .setLink(url)
                .setSikkerhetsnivaa(SIKKERHETSNIVÅ)
                .setTekst(tekst)
                .setTidspunkt(Instant.now().toEpochMilli()).build();
    }

    static Nokkel beskjedNøkkel(String eventId) {
        return Nokkel.newBuilder()
                .setEventId(BESKJED + eventId)
                .setSystembruker(SYSTEMBRUKER)
                .build();
    }

    static Nokkel oppgaveNøkkel(String eventId) {
        return Nokkel.newBuilder()
                .setEventId(OPPGAVE + eventId)
                .setSystembruker(SYSTEMBRUKER)
                .build();
    }

}
