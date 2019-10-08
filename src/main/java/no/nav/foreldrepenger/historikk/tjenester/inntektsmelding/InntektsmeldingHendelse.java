package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import java.time.LocalDate;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.tjenester.felles.Hendelse;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;

@Valid
public class InntektsmeldingHendelse extends Hendelse {

    private final LocalDate innsendingsDato;
    private final Arbeidsgiver arbeidsgiver;

    @JsonCreator
    public InntektsmeldingHendelse(@JsonProperty("aktørId") AktørId aktørId, String journalId,
            String referanseId,
            String saksNr, LocalDate innsendingsDato, Arbeidsgiver arbeidsgiver) {
        super(aktørId, journalId, referanseId, saksNr, HendelseType.INNTEKTSMELDING);
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
                + ", aktørId=" + getAktørId() + ", journalId=" + getJournalId()
                + ", referanseId=" + getReferanseId() + ", saksnr=" + getSaksNr() + "]";
    }

}
