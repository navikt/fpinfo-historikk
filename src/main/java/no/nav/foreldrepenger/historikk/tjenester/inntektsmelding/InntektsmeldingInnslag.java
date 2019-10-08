package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag;

public class InntektsmeldingInnslag extends HistorikkInnslag {

    private ArbeidsgiverInnslag arbeidsgiver;

    public ArbeidsgiverInnslag getArbeidsgiver() {
        return arbeidsgiver;
    }

    public void setArbeidsgiver(ArbeidsgiverInnslag arbeidsgiver) {
        this.arbeidsgiver = arbeidsgiver;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[aktørId=" + getAktørId() + ", journalpostId="
                + getJournalpostId() + ", saksnr=" + getSaksnr() + ", opprettet=" + opprettet + ", arbeidsgiver="
                + arbeidsgiver
                + "]";
    }

}
