package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static no.nav.foreldrepenger.historikk.tjenester.journalføring.JournalpostType.UTGAAENDE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.historikk.tjenester.journalføring.AvsenderMottaker;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.Bruker;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.Dokument;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.DokumentVariant;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.Journalpost;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.Sak;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.dao.JPAMinidialogInnslag;

public final class MinidialogMapper {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogMapper.class);

    private static final String SPØRSMÅL = "Spørsmål fra saksbehandler";

    private MinidialogMapper() {

    }

    static JPAMinidialogInnslag fraInnslag(MinidialogInnslag m) {
        JPAMinidialogInnslag dialog = new JPAMinidialogInnslag();
        dialog.setAktørId(m.getAktørId());
        dialog.setFnr(m.getFnr());
        dialog.setJanei(m.isJanei());
        dialog.setVedlegg(m.getVedlegg());
        dialog.setTekst(m.getTekst());
        dialog.setSaksnr(m.getSaksnr());
        dialog.setGyldigTil(m.getGyldigTil());
        dialog.setHandling(m.getHandling());
        dialog.setAktiv(m.isAktiv());
        return dialog;
    }

    static Journalpost journalpostFra(MinidialogInnslag innslag, String navn, byte[] dokument) {
        return new Journalpost(UTGAAENDE,
                new AvsenderMottaker(innslag.getFnr(), navn),
                new Bruker(innslag.getFnr()), null,
                SPØRSMÅL,
                new Sak(innslag.getSaksnr()),
                new Dokument(new DokumentVariant(dokument)));
    }

    static MinidialogInnslag tilInnslag(JPAMinidialogInnslag i) {
        LOG.info("Mapper fra innslag {}", i);
        MinidialogInnslag innslag = new MinidialogInnslag(i.getAktørId(), i.getTekst(), i.getSaksnr());
        innslag.setEndret(i.getEndret());
        innslag.setOpprettet(i.getOpprettet());
        innslag.setId(i.getId());
        innslag.setGyldigTil(i.getGyldigTil());
        innslag.setHandling(i.getHandling());
        innslag.setAktiv(i.isAktiv());
        LOG.info("Mapper til innslag {}", innslag);
        return innslag;
    }
}
