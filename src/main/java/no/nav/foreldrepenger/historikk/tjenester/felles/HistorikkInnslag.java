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
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogInnslag;
import no.nav.foreldrepenger.historikk.tjenester.søknad.SøknadInnslag;

@JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
@JsonSubTypes({
        @Type(value = SøknadInnslag.class, name = "søknad"),
        @Type(value = InntektsmeldingInnslag.class, name = "inntekt"),
        @Type(value = MinidialogInnslag.class, name = "minidialog")

})
public abstract class HistorikkInnslag implements Comparable<HistorikkInnslag> {

    public static final Sort SORT_OPPRETTET_ASC = new Sort(ASC, "opprettet");

    private final Fødselsnummer fnr;
    private AktørId aktørId;
    private String journalpostId;
    private String saksnr;
    protected LocalDateTime opprettet;

    public HistorikkInnslag(Fødselsnummer fnr) {
        this.fnr = fnr;
    }

    protected HendelseType hendelseFra(String hendelse) {
        return Optional.ofNullable(hendelse)
                .map(HendelseType::tilHendelse)
                .orElse(UKJENT);
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

    public Fødselsnummer getFnr() {
        return fnr;
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public void setJournalpostId(String journalpostId) {
        this.journalpostId = journalpostId;
    }

    @Override
    public int compareTo(HistorikkInnslag o) {
        return opprettet.compareTo(o.getOpprettet());
    }

}
