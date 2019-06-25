package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import java.util.List;

import no.nav.foreldrepenger.historikk.util.Pair;

public class Journalpost {

    private final JournalpostType journalpostType;
    private final AvsenderMottaker avsenderMottaker;
    private final Bruker bruker;
    private final BehandlingTema behandlingstema;
    private final String tittel;
    private final String journalfoerendeEnhet;

    private final String eksternReferanseId;
    private final String tema;
    List<Pair<String, String>> tilleggsOpplysninger;
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

    public BehandlingTema getBehandlingstema() {
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

    public List<Pair<String, String>> getTilleggsOpplysninger() {
        return tilleggsOpplysninger;
    }

    public Sak getSak() {
        return sak;
    }

    public List<Dokument> getDokumenter() {
        return dokumenter;
    }

    public Journalpost(JournalpostType journalpostType, AvsenderMottaker avsenderMotaker, Bruker bruker,
            BehandlingTema behandlingstema, String tittel, String journalfoerendeEnhet, String eksternReferanseId,
            List<Pair<String, String>> tilleggsOpplysninger, Sak sak, List<Dokument> dokumenter) {
        this.journalpostType = journalpostType;
        this.avsenderMottaker = avsenderMotaker;
        this.bruker = bruker;
        this.tema = "FOR";
        this.behandlingstema = behandlingstema;
        this.tittel = tittel;
        this.journalfoerendeEnhet = journalfoerendeEnhet;
        this.eksternReferanseId = eksternReferanseId;
        this.tilleggsOpplysninger = tilleggsOpplysninger;
        this.sak = sak;
        this.dokumenter = dokumenter;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[journalportType=" + journalpostType + ", avsenderMotaker="
                + avsenderMottaker + ", bruker=" + bruker + ", behandlingstema=" + behandlingstema + ", tittel="
                + tittel
                + ", journalførendeEnhet=" + journalfoerendeEnhet + ", externReferanseId=" + externReferanseId
                + ", tilleggsOpplysninger=" + tilleggsOpplysninger + ", sak=" + sak + ", dokumenter=" + dokumenter
                + "]";
    }
}
