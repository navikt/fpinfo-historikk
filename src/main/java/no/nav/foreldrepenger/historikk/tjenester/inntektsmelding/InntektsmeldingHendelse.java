package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import java.time.LocalDate;

import javax.validation.Valid;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Versjon;
import no.nav.foreldrepenger.historikk.tjenester.felles.Hendelse;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;

@Valid
public class InntektsmeldingHendelse extends Hendelse {

    private final LocalDate innsendingsDato;
    private final String arbeidsgiver;
    private final String referanseId;
    private final Versjon versjon;

    public InntektsmeldingHendelse(AktørId aktørId, String journalId, String referanseId,
            String saksNr, HendelseType hendelse, LocalDate innsendingsDato, String arbeidsgiver, Versjon versjon) {
        super(aktørId, journalId, saksNr, hendelse);
        this.innsendingsDato = innsendingsDato;
        this.arbeidsgiver = arbeidsgiver;
        this.referanseId = referanseId;
        this.versjon = versjon;
    }

    public Versjon getVersjon() {
        return versjon;
    }

    public LocalDate getInnsendingsDato() {
        return innsendingsDato;
    }

    public String getReferanseId() {
        return referanseId;
    }

    public String getArbeidsgiver() {
        return arbeidsgiver;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[versjon=" + versjon + ", innsendingsDato=" + innsendingsDato
                + ", arbeidsgiver=" + arbeidsgiver
                + ", aktørId=" + getAktørId() + ", journalId=" + getJournalId()
                + ", referanseId=" + getReferanseId() + ", saksnr=" + getSaksnummer() + "]";
    }
}
