package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import java.time.LocalDateTime;

import javax.validation.Valid;

import io.swagger.annotations.ApiModelProperty;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Versjon;
import no.nav.foreldrepenger.historikk.tjenester.felles.Hendelse;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;

@Valid
public class InntektsmeldingHendelse extends Hendelse {

    @ApiModelProperty(example = "2019-10-22T18:50:10.9851661")
    private final LocalDateTime innsendingsTidspunkt;
    @ApiModelProperty(example = "888888888")
    private final String arbeidsgiver;
    @ApiModelProperty(example = "AR123456789")
    private final String referanseId;
    private final Versjon versjon;
    @ApiModelProperty(example = "NY")
    private final InntektsmeldingType imType;

    public InntektsmeldingHendelse(AktørId aktørId, String journalId, String referanseId, String saksNr,
            HendelseType hendelse, LocalDateTime innsendingsTidspunkt, String arbeidsgiver, Versjon versjon,
            InntektsmeldingType imType) {
        super(aktørId, journalId, saksNr, hendelse);
        this.innsendingsTidspunkt = innsendingsTidspunkt;
        this.arbeidsgiver = arbeidsgiver;
        this.referanseId = referanseId;
        this.versjon = versjon;
        this.imType = imType;
    }

    public InntektsmeldingType getImType() {
        return imType;
    }

    public Versjon getVersjon() {
        return versjon;
    }

    public LocalDateTime getInnsendingsTidspunkt() {
        return innsendingsTidspunkt;
    }

    public String getReferanseId() {
        return referanseId;
    }

    public String getArbeidsgiver() {
        return arbeidsgiver;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[versjon=" + versjon + ", innsendingsTidspunkt=" + innsendingsTidspunkt
                + ", arbeidsgiver=" + arbeidsgiver
                + ", aktørId=" + getAktørId() + ", journalId=" + getJournalId()
                + ", referanseId=" + getReferanseId() + ", saksnr=" + getSaksnummer() + "]";
    }
}
