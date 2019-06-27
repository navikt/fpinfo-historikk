package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static no.nav.foreldrepenger.historikk.tjenester.journalføring.JournalpostType.UTGAAENDE;

import no.nav.foreldrepenger.historikk.tjenester.journalføring.AvsenderMottaker;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.Bruker;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.Dokument;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.DokumentVariant;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.Journalpost;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.Sak;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogInnslag;

public final class MinidialogMapper {

    private static final String SPØRSMÅL = "Spørsmål fra saksbehandler";

    private MinidialogMapper() {

    }

    static JPAMinidialogInnslag fraInnslag(MinidialogInnslag m) {
        JPAMinidialogInnslag dialog = new JPAMinidialogInnslag(m.getAktørId(), m.getMelding(),
                m.getSaksnr());
        dialog.setGyldigTil(m.getGyldigTil());
        dialog.setHandling(m.getHandling());
        dialog.setAktiv(m.isAktiv());
        return dialog;
    }

    static Journalpost journalpostFra(MinidialogInnslag innslag, byte[] dokument) {
        return new Journalpost(UTGAAENDE,
                new AvsenderMottaker(innslag.getFnr()),
                new Bruker(innslag.getFnr()), null,
                SPØRSMÅL,
                new Sak(innslag.getSaksnr()),
                new Dokument(new DokumentVariant(dokument)));
    }

    static MinidialogInnslag tilInnslag(JPAMinidialogInnslag m) {
        MinidialogInnslag melding = new MinidialogInnslag(m.getAktørId(), m.getMelding(),
                m.getSaksnr());
        melding.setEndret(m.getEndret());
        melding.setOpprettet(m.getOpprettet());
        melding.setId(m.getId());
        melding.setGyldigTil(m.getGyldigTil());
        melding.setHandling(m.getHandling());
        melding.setAktiv(m.isAktiv());
        return melding;
    }
}
