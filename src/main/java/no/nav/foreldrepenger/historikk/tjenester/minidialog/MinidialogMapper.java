package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import java.util.Collections;

import no.nav.foreldrepenger.historikk.tjenester.innsending.SøknadType;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.AvsenderMottaker;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.Bruker;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.IdType;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.Journalpost;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.JournalpostType;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.Sak;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogInnslag;

public final class MinidialogMapper {

    private MinidialogMapper() {

    }

    static JPAMinidialogInnslag fraInnslag(MinidialogInnslag m) {
        JPAMinidialogInnslag dialog = new JPAMinidialogInnslag(m.getAktørId(), m.getMelding(),
                m.getSaksnr(),
                m.getKanal().name());
        dialog.setGyldigTil(m.getGyldigTil());
        dialog.setHandling(m.getHandling().name());
        dialog.setAktiv(m.isAktiv());
        return dialog;
    }

    static Journalpost journalpostFra(MinidialogInnslag innslag) {
        return new Journalpost(JournalpostType.UTGAAENDE,
                new AvsenderMottaker(innslag.getFnr(), IdType.FNR, "Spørsmål fra saksbehandler"),
                new Bruker(innslag.getFnr()), innslag.getHandling().tema(),
                "tittel",
                Collections.emptyList(), new Sak(innslag.getSaksnr()),
                Collections.emptyList());
    }

    static MinidialogInnslag tilInnslag(JPAMinidialogInnslag m) {
        MinidialogInnslag melding = new MinidialogInnslag(m.getAktørId(), m.getMelding(),
                m.getSaksnr());
        melding.setEndret(m.getEndret());
        melding.setOpprettet(m.getOpprettet());
        melding.setKanal(LeveranseKanal.valueOf(m.getKanal()));
        melding.setId(m.getId());
        melding.setGyldigTil(m.getGyldigTil());
        if (m.getHandling() != null) {
            melding.setHandling(SøknadType.valueOf(m.getHandling()));
        }
        melding.setAktiv(m.isAktiv());
        return melding;
    }

}
