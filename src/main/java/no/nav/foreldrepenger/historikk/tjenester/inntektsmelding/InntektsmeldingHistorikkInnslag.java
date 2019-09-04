package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import static no.nav.foreldrepenger.historikk.tjenester.Hendelse.UKJENT;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.Hendelse;

class InntektsmeldingHistorikkInnslag {

    private static final Logger LOG = LoggerFactory.getLogger(InntektsmeldingHistorikkInnslag.class);
    private final Fødselsnummer fnr;
    private AktørId aktørId;
    private String journalpostId;
    private String saksnr;
    private LocalDateTime opprettet;
    private Arbeidsgiver arbeidsgiver;

    @JsonCreator
    public InntektsmeldingHistorikkInnslag(@JsonProperty("fnr") Fødselsnummer fnr) {
        this.fnr = fnr;
    }

    private Hendelse hendelseFra(String hendelse) {
        return Optional.ofNullable(hendelse)
                .map(Hendelse::tilHendelse)
                .orElse(UKJENT);
    }

    public void setAktørId(AktørId aktørId) {
        this.aktørId = aktørId;
    }

    public AktørId getAktørId() {
        return aktørId;
    }

    public Fødselsnummer getFnr() {
        return fnr;
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public void setJournalpostId(String journalpostId) {
        this.journalpostId = journalpostId;
    }

    public LocalDateTime getOpprettet() {
        return opprettet;
    }

    public Arbeidsgiver getArbeidsgiver() {
        return arbeidsgiver;
    }

    public void setArbeidsgiver(Arbeidsgiver arbeidsgiver) {
        this.arbeidsgiver = arbeidsgiver;
    }

    public void setOpprettet(LocalDateTime opprettet) {
        this.opprettet = opprettet;
    }

    public String getSaksnr() {
        return saksnr;
    }

    public void setSaksnr(String saksnr) {
        this.saksnr = saksnr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[fnr=" + fnr + ", aktørId=" + aktørId + ", journalpostId="
                + journalpostId + ", saksnr=" + saksnr + ", opprettet=" + opprettet + ", arbeidsgiver=" + arbeidsgiver
                + "]";
    }

}
