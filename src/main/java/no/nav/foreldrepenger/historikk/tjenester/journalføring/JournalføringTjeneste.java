package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import org.springframework.stereotype.Service;

@Service
public class JournalføringTjeneste {

    private final JournalføringConnection connection;

    public JournalføringTjeneste(JournalføringConnection connection) {
        this.connection = connection;
    }

    public String journalfør(Journalpost journalpost, boolean sluttfør) {
        return connection.opprettJournalpost(journalpost, sluttfør).getJournalpostId();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[connection=" + connection + "]";
    }

}
