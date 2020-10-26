package no.nav.foreldrepenger.historikk.tjenester.oppslag;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = ANY)
record Navn(String fornavn, String mellomnavn, String etternavn, String kjønn) {

}
