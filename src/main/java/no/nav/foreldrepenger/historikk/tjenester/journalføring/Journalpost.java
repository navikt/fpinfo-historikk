package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import java.util.List;

import no.nav.foreldrepenger.historikk.util.MDCUtil;
import no.nav.foreldrepenger.historikk.util.Pair;

public class Journalpost {

    private final JournalpostType journalpostType;
    private final AvsenderMottaker avsenderMottaker;
    private final Bruker bruker;
    private final String behandlingstema;
    private final String tittel;
    private final String journalfoerendeEnhet;

    private final String eksternReferanseId;
    private final String tema;
    List<Pair<String, String>> tilleggsopplysninger;
    private final Sak sak;
    private final List<Dokument> dokumenter;

    public JournalpostType getJournalpostType() {
        return journalpostType;
    }

    public AvsenderMottaker getAvsenderMottaker() {
        return avsenderMottaker;
    }

    public Bruker getBruker() {
        return bruker;
    }

    public String getBehandlingstema() {
        return behandlingstema;
    }

    public String getTittel() {
        return tittel;
    }

    public String getJournalfoerendeEnhet() {
        return journalfoerendeEnhet;
    }

    public String getEksternReferanseId() {
        return eksternReferanseId;
    }

    public String getTema() {
        return tema;
    }

    public List<Pair<String, String>> getTilleggsopplysninger() {
        return tilleggsopplysninger;
    }

    public Sak getSak() {
        return sak;
    }

    public List<Dokument> getDokumenter() {
        return dokumenter;
    }

    public Journalpost(JournalpostType journalpostType, AvsenderMottaker avsenderMotaker, Bruker bruker,
            String behandlingstema, String tittel,
            List<Pair<String, String>> tilleggsopplysninger, Sak sak, List<Dokument> dokumenter) {
        this.journalpostType = journalpostType;
        this.avsenderMottaker = avsenderMotaker;
        this.bruker = bruker;
        this.tema = "FOR";
        this.behandlingstema = behandlingstema;
        this.tittel = tittel;
        this.journalfoerendeEnhet = "9999";
        this.eksternReferanseId = MDCUtil.callIdOrNew();
        this.tilleggsopplysninger = tilleggsopplysninger;
        this.sak = sak;
        this.dokumenter = dokumenter;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[journalportType=" + journalpostType + ", avsenderMotaker="
                + avsenderMottaker + ", bruker=" + bruker + ", behandlingstema=" + behandlingstema + ", tittel="
                + tittel
                + ", journalførendeEnhet=" + journalfoerendeEnhet + ", externReferanseId=" + eksternReferanseId
                + ", tilleggsopplysninger=" + tilleggsopplysninger + ", sak=" + sak + ", dokumenter=" + dokumenter
                + "]";
    }
}
