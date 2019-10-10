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

    public InntektsmeldingHendelse(AktørId aktørId, String journalId,
            String referanseId, String dialogId,
            String saksNr, LocalDate innsendingsDato, ArbeidsgiverInnslag arbeidsgiver) {
        super(aktørId, journalId, referanseId, dialogId, saksNr, HendelseType.INNTEKTSMELDING);
        this.innsendingsDato = innsendingsDato;
        this.arbeidsgiver = arbeidsgiver;
    }

    public LocalDate getInnsendingsDato() {
        return innsendingsDato;
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
