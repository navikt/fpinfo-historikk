package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import no.nav.foreldrepenger.historikk.domain.Versjon;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag;

public class InntektsmeldingInnslag extends HistorikkInnslag {

    private ArbeidsgiverInnslag arbeidsgiver;

    private Versjon versjon;

    public Versjon getVersjon() {
        return versjon;
    }

    public void setVersjon(Versjon versjon) {
        this.versjon = versjon;
    }

    public ArbeidsgiverInnslag getArbeidsgiver() {
        return arbeidsgiver;
    }

    public void setArbeidsgiver(ArbeidsgiverInnslag arbeidsgiver) {
        this.arbeidsgiver = arbeidsgiver;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[versjon=" + versjon + ", aktørId=" + getAktørId() + ", journalpostId="
                + getJournalpostId() + ", saksnr=" + getSaksnr() + ", opprettet=" + opprettet + ", arbeidsgiver="
                + arbeidsgiver + "]";
    }

}
