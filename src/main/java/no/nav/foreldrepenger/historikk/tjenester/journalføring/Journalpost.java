package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import java.util.Arrays;
import java.util.List;

import no.nav.foreldrepenger.historikk.util.MDCUtil;

public class Journalpost {

    private static final String AUTOMATISK = "9999";
    private static final String FORELDREPENGER = "FOR";
    private final JournalpostType journalpostType;
    private final AvsenderMottaker avsenderMottaker;
    private final Bruker bruker;
    private final String behandlingstema;
    private final String tittel;

    private final String eksternReferanseId;
    private final Sak sak;
    private final List<Dokument> dokumenter;

    public Journalpost(JournalpostType journalpostType, AvsenderMottaker avsenderMotaker, Bruker bruker,
            String behandlingstema, String tittel,
            Sak sak, Dokument... dokumenter) {
        this.journalpostType = journalpostType;
        this.avsenderMottaker = avsenderMotaker;
        this.bruker = bruker;
        this.behandlingstema = behandlingstema;
        this.tittel = tittel;
        this.eksternReferanseId = MDCUtil.callIdOrNew();
        this.sak = sak;
        this.dokumenter = Arrays.asList(dokumenter);
    }

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
        return AUTOMATISK;
    }

    public String getEksternReferanseId() {
        return eksternReferanseId;
    }

    public String getTema() {
        return FORELDREPENGER;
    }

    public Sak getSak() {
        return sak;
    }

    public List<Dokument> getDokumenter() {
        return dokumenter;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[journalportType=" + journalpostType + ", avsenderMotaker="
                + avsenderMottaker + ", bruker=" + bruker + ", behandlingstema=" + behandlingstema + ", tittel="
                + tittel
                + ", externReferanseId=" + eksternReferanseId
                + ", sak=" + sak + ", dokumenter=" + dokumenter
                + "]";
    }
}
