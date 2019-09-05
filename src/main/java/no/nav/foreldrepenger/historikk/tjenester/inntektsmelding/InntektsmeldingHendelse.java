package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import java.time.LocalDate;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.Hendelse;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingHendelse;

@Valid
public class InntektsmeldingHendelse extends InnsendingHendelse {

    private final LocalDate innsendingsDato;
    private final Arbeidsgiver arbeidsgiver;

    @JsonCreator
    public InntektsmeldingHendelse(@JsonProperty("aktørId") AktørId aktørId, Fødselsnummer fnr, String journalId,
            String referanseId,
            String saksNr, LocalDate innsendingsDato, Arbeidsgiver arbeidsgiver) {
        super(aktørId, fnr, journalId, referanseId, saksNr, Hendelse.INNTEKTSMELDING);
        this.innsendingsDato = innsendingsDato;
        this.arbeidsgiver = arbeidsgiver;
    }

    public LocalDate getInnsendingsDato() {
        return innsendingsDato;
    }

    public Arbeidsgiver getArbeidsgiver() {
        return arbeidsgiver;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsendingsDato=" + innsendingsDato + ", arbeidsgiver=" + arbeidsgiver
                + ", aktørId=" + getAktørId() + ", fnr=" + getFnr() + ", journalId=" + getJournalId()
                + ", referanseId=" + getReferanseId() + ", saksnr=" + getSaksNr() + ", hendelse="
                + getHendelse() + "]";
    }

}
