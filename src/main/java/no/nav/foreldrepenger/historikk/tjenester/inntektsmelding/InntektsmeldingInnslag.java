package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag;

public class InntektsmeldingInnslag extends HistorikkInnslag {

    private ArbeidsgiverInnslag arbeidsgiver;

    @JsonCreator
    public InntektsmeldingInnslag(@JsonProperty("fnr") Fødselsnummer fnr) {
        super(fnr);
    }

    public ArbeidsgiverInnslag getArbeidsgiver() {
        return arbeidsgiver;
    }

    public void setArbeidsgiver(ArbeidsgiverInnslag arbeidsgiver) {
        this.arbeidsgiver = arbeidsgiver;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[fnr=" + getFnr() + ", aktørId=" + getAktørId() + ", journalpostId="
                + getJournalpostId() + ", saksnr=" + getSaksnr() + ", opprettet=" + opprettet + ", arbeidsgiver="
                + arbeidsgiver
                + "]";
    }

}
