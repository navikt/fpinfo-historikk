package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import java.util.List;

import no.nav.foreldrepenger.historikk.util.Pair;

public class Journalpost {

    private final JournalpostType journalpostType;
    private final AvsenderMottaker avsenderMotaker;
    private final Bruker bruker;
    private final Behandlingstema behandlingstema;
    private final String tittel;
    private final String journalførendeEnhet;
    private final String externReferanseId;
    List<Pair<String, String>> tilleggsOpplysninger;
    private final Sak sak;
    private final List<Dokument> dokumenter;

    public Journalpost(JournalpostType journalpostType, AvsenderMottaker avsenderMotaker, Bruker bruker,
            Behandlingstema behandlingstema, String tittel, String journalførendeEnhet, String externReferanseId,
            List<Pair<String, String>> tilleggsOpplysninger, Sak sak, List<Dokument> dokumenter) {
        this.journalpostType = journalpostType;
        this.avsenderMotaker = avsenderMotaker;
        this.bruker = bruker;
        this.behandlingstema = behandlingstema;
        this.tittel = tittel;
        this.journalførendeEnhet = journalførendeEnhet;
        this.externReferanseId = externReferanseId;
        this.tilleggsOpplysninger = tilleggsOpplysninger;
        this.sak = sak;
        this.dokumenter = dokumenter;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[journalportType=" + journalpostType + ", avsenderMotaker="
                + avsenderMotaker + ", bruker=" + bruker + ", behandlingstema=" + behandlingstema + ", tittel=" + tittel
                + ", journalførendeEnhet=" + journalførendeEnhet + ", externReferanseId=" + externReferanseId
                + ", tilleggsOpplysninger=" + tilleggsOpplysninger + ", sak=" + sak + ", dokumenter=" + dokumenter
                + "]";
    }
}
