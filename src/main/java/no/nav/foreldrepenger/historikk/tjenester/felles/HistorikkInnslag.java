package no.nav.foreldrepenger.historikk.tjenester.felles;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType.UKJENT;
import static org.springframework.data.domain.Sort.Direction.ASC;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.tilbakekreving.TilbakekrevingInnslag;

@JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
@JsonSubTypes({
        @Type(value = InnsendingInnslag.class, name = "søknad"),
        @Type(value = InntektsmeldingInnslag.class, name = "inntekt"),
        @Type(value = TilbakekrevingInnslag.class, name = "minidialog")

})
public abstract class HistorikkInnslag implements Comparable<HistorikkInnslag> {

    public static final Sort SORT_OPPRETTET_ASC = Sort.by(ASC, "opprettet");
    public static final Sort SORT_ENDRET_ASC = Sort.by(ASC, "endret");

    private AktørId aktørId;
    private Fødselsnummer fnr;

    private String journalpostId;
    private String saksnr;
    private String referanseId;
    private LocalDateTime innsendt;

    protected LocalDateTime opprettet;

    protected HendelseType hendelseFra(String hendelse) {
        return Optional.ofNullable(hendelse)
                .map(HendelseType::tilHendelse)
                .orElse(UKJENT);
    }

    public String getReferanseId() {
        return referanseId;
    }

    public void setReferanseId(String referanseId) {
        this.referanseId = referanseId;
    }

    public String getSaksnr() {
        return saksnr;
    }

    public void setSaksnr(String saksnr) {
        this.saksnr = saksnr;
    }

    public LocalDateTime getOpprettet() {
        return opprettet;
    }

    public void setOpprettet(LocalDateTime opprettet) {
        this.opprettet = opprettet;
    }

    public void setAktørId(AktørId aktørId) {
        this.aktørId = aktørId;
    }

    public AktørId getAktørId() {
        return aktørId;
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public void setJournalpostId(String journalpostId) {
        this.journalpostId = journalpostId;
    }

    public LocalDateTime getInnsendt() {
        return innsendt;
    }

    public void setInnsendt(LocalDateTime innsendt) {
        this.innsendt = innsendt;
    }

    public Fødselsnummer getFnr() {
        return fnr;
    }

    public void setFnr(Fødselsnummer fnr) {
        this.fnr = fnr;
    }

    @Override
    public int compareTo(HistorikkInnslag o) {
        return opprettet.compareTo(o.getOpprettet());
    }

}
