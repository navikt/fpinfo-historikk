package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingType.LASTET_OPP;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingType.SEND_SENERE;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag;

public class InnsendingInnslag extends HistorikkInnslag {

    private final HendelseType hendelse;
    private List<Pair<String, InnsendingType>> vedlegg;
    private LocalDate behandlingsdato;

    @JsonCreator
    public InnsendingInnslag(@JsonProperty("hendelse") HendelseType hendelse) {
        this.hendelse = hendelse;
    }

    public LocalDate getBehandlingsdato() {
        return behandlingsdato;
    }

    public void setBehandlingsdato(LocalDate behandlingsdato) {
        this.behandlingsdato = behandlingsdato;
    }

    public List<String> getVedlegg() {
        return vedlegg.stream()
                .map(Pair::getLeft)
                .toList();
    }

    public List<String> getOpplastedeVedlegg() {
        return vedlegg.stream()
                .filter(v -> LASTET_OPP.equals(v.getRight()))
                .map(Pair::getLeft)
                .toList();
    }

    public List<String> getIkkeOpplastedeVedlegg() {
        return vedlegg.stream()
                .filter(v -> SEND_SENERE.equals(v.getRight()))
                .map(Pair::getLeft)
                .toList();
    }

    public void setVedlegg(List<Pair<String, InnsendingType>> vedlegg) {
        this.vedlegg = vedlegg;
    }

    public HendelseType getHendelse() {
        return hendelse;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[hendelse=" + hendelse + ", behandlingsdato=" + behandlingsdato
                + ", vedlegg=" + vedlegg + ", referanseId=" + getReferanseId() + ", saksnr=" + getSaksnr()
                + ", opprettet=" + getOpprettet() + ", aktørId=" + getAktørId() + ", journalpostId="
                + getJournalpostId() + ", innsendt=" + getInnsendt() + ", fnr=" + getFnr() + "]";
    }

}
