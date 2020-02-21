package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import java.time.Instant;

import no.nav.brukernotifikasjon.schemas.Beskjed;
import no.nav.brukernotifikasjon.schemas.Done;
import no.nav.brukernotifikasjon.schemas.Oppgave;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingHendelse;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogHendelse;

public class DittNavMapper {

    private DittNavMapper() {

    }

    static Oppgave oppgave(MinidialogHendelse h, String url) {
        return Oppgave.newBuilder()
                .setFodselsnummer(h.getFnr().getFnr())
                .setGrupperingsId(h.getSaksnummer())
                .setSikkerhetsnivaa(3)
                .setLink(url)
                .setTekst(h.getHendelse().beskrivelse)
                .setTidspunkt(Instant.now().toEpochMilli()).build();
    }

    static Done done(Fødselsnummer fnr, String grupperingsId) {
        return Done.newBuilder()
                .setFodselsnummer(fnr.getFnr())
                .setGrupperingsId(grupperingsId)
                .setTidspunkt(Instant.now().toEpochMilli()).build();
    }

    static Beskjed beskjed(InnsendingHendelse h, String url) {
        return Beskjed.newBuilder()
                .setFodselsnummer(h.getFnr().getFnr()).setGrupperingsId(h.getSaksnummer())
                .setLink(url)
                .setSikkerhetsnivaa(3)
                .setSynligFremTil(null)
                .setTekst(h.getHendelse().beskrivelse)
                .setTidspunkt(Instant.now().toEpochMilli()).build();
    }

}
