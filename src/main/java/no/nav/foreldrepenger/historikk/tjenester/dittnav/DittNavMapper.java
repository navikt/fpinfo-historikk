package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;

import no.nav.brukernotifikasjon.schemas.builders.BeskjedInputBuilder;
import no.nav.brukernotifikasjon.schemas.builders.DoneInputBuilder;
import no.nav.brukernotifikasjon.schemas.builders.NokkelInputBuilder;
import no.nav.brukernotifikasjon.schemas.builders.OppgaveInputBuilder;
import no.nav.brukernotifikasjon.schemas.input.BeskjedInput;
import no.nav.brukernotifikasjon.schemas.input.DoneInput;
import no.nav.brukernotifikasjon.schemas.input.NokkelInput;
import no.nav.brukernotifikasjon.schemas.input.OppgaveInput;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

final class DittNavMapper {

    private static final String APPNAVN = "fpinfo-historikk";
    private static final String NAMESPACE = "teamforeldrepenger";
    private static final int SIKKERHETSNIVÅ = 3;


    private DittNavMapper() {

    }


    static NokkelInput nøkkel(Fødselsnummer fnr, String eventId, String grupperingsId) {
        return new NokkelInputBuilder()
            .withFodselsnummer(fnr.getFnr())
            .withEventId(eventId)
            .withGrupperingsId(grupperingsId)
            .withAppnavn(APPNAVN)
            .withNamespace(NAMESPACE)
            .build();
    }

    static NokkelInput avsluttNøkkel(Fødselsnummer fnr, String eventId, String grupperingsId) {
        // Bruker Avroskjemaobjekt direkte for å støtte legacy eventId
        return new NokkelInput(eventId, grupperingsId, fnr.getFnr(), NAMESPACE, APPNAVN);
    }

    static BeskjedInput beskjed(String tekst, URI landingsside, Duration duration) {
        return new BeskjedInputBuilder()
            .withSikkerhetsnivaa(SIKKERHETSNIVÅ)
            .withTidspunkt(LocalDateTime.now())
            .withLink(toUrl(landingsside))
            .withSynligFremTil(LocalDateTime.now().plus(duration))
            .withTekst(tekst)
            .build();
    }

    static OppgaveInput oppgave(String tekst, URI landingsside) {
        return new OppgaveInputBuilder()
            .withTidspunkt(LocalDateTime.now())
            .withLink(toUrl(landingsside))
            .withSikkerhetsnivaa(SIKKERHETSNIVÅ)
            .withTekst(tekst)
            .build();
    }

    static DoneInput avslutt() {
        return new DoneInputBuilder()
            .withTidspunkt(LocalDateTime.now())
            .build();
    }

    static URL toUrl(URI uri) {
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Kunne ikke lage URL", e);
        }
    }

}
