package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Versjon;
import no.nav.foreldrepenger.historikk.tjenester.felles.Hendelse;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;

@Valid
public class InntektsmeldingHendelse extends Hendelse {

    @ApiModelProperty(example = "888888888")
    private final String arbeidsgiverId;
    @ApiModelProperty(example = "AR123456789")
    private final String referanseId;
    @JsonAlias("version")
    private final Versjon versjon;
    private final LocalDate startDato;

    @JsonCreator
    public InntektsmeldingHendelse(
            @JsonProperty("aktørId") AktørId aktørId,
            @JsonProperty("journalpostId") String journalpostId,
            @JsonProperty("saksnummer") String saksnummer,
            @JsonProperty("referanseId") String referanseId,
            @JsonProperty("hendelse") HendelseType hendelse,
            @JsonProperty("innsendingsTidspunkt") LocalDateTime innsendingsTidspunkt,
            @JsonProperty("arbeidsgiverId") String arbeidsgiverId,
            @JsonProperty("versjon") Versjon versjon,
            @JsonProperty("startDato") LocalDate startDato,
            @JsonProperty("innsendt") LocalDateTime innsendt) {
        super(aktørId, journalpostId, null, saksnummer, hendelse, innsendt);
        this.arbeidsgiverId = arbeidsgiverId;
        this.referanseId = referanseId;
        this.versjon = versjon;
        this.startDato = startDato;
    }

    public LocalDate getStartDato() {
        return startDato;
    }

    public Versjon getVersjon() {
        return versjon;
    }

    public String getReferanseId() {
        return referanseId;
    }

    public String getArbeidsgiverId() {
        return arbeidsgiverId;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[versjon=" + versjon
                + ", arbeidsgiver=" + arbeidsgiverId
                + ", aktørId=" + getAktørId() + ", journalpostId=" + getJournalpostId()
                + ", referanseId=" + getReferanseId() + ", saksnr=" + getSaksnummer() + "]";
    }
}
