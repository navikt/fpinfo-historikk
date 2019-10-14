package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import java.time.LocalDate;

import javax.validation.Valid;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.tjenester.felles.Hendelse;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;

@Valid
public class InntektsmeldingHendelse extends Hendelse {

    private final LocalDate innsendingsDato;
    private final ArbeidsgiverInnslag arbeidsgiver;
    private final String referanseId;

    public InntektsmeldingHendelse(AktørId aktørId, String journalId, String referanseId,
            String saksNr, HendelseType hendelseType, LocalDate innsendingsDato, ArbeidsgiverInnslag arbeidsgiver) {
        super(aktørId, journalId, saksNr, hendelseType);
        this.innsendingsDato = innsendingsDato;
        this.arbeidsgiver = arbeidsgiver;
        this.referanseId = referanseId;
    }

    public LocalDate getInnsendingsDato() {
        return innsendingsDato;
    }

    public String getReferanseId() {
        return referanseId;
    }

    public ArbeidsgiverInnslag getArbeidsgiver() {
        return arbeidsgiver;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsendingsDato=" + innsendingsDato + ", arbeidsgiver=" + arbeidsgiver
                + ", aktørId=" + getAktørId() + ", journalId=" + getJournalId()
                + ", referanseId=" + getReferanseId() + ", saksnr=" + getSaksNr() + "]";
    }

}
