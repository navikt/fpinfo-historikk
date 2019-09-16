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

    static JPAMinidialogInnslag fraInnslag(MinidialogHendelse m) {
        LOG.info("Mapper fra {}", m);
        JPAMinidialogInnslag dialog = new JPAMinidialogInnslag();
        dialog.setAktørId(m.getAktørId());
        dialog.setFnr(m.getFnr());
        dialog.setTekst(m.getTekst());
        dialog.setSaksnr(m.getSaksnr());
        dialog.setGyldigTil(m.getGyldigTil());
        dialog.setAktiv(m.isAktiv());
        LOG.info("Mappet til {}", dialog);
        return dialog;
    }

    static Journalpost journalpostFra(MinidialogHendelse innslag, byte[] dokument) {
        return new Journalpost(UTGAAENDE,
                new AvsenderMottaker(innslag.getFnr(), innslag.getNavn()),
                new Bruker(innslag.getFnr()), null,
                SPØRSMÅL,
                new Sak(innslag.getSaksnr()),
                new Dokument(new DokumentVariant(dokument)));
    }

    static MinidialogHendelse tilInnslag(JPAMinidialogInnslag i) {
        LOG.info("Mapper fra innslag {}", i);
        MinidialogHendelse innslag = new MinidialogHendelse(i.getFnr(), i.getTekst());
        innslag.setEndret(i.getEndret());
        innslag.setAktørId(i.getAktørId());
        innslag.setOpprettet(i.getOpprettet());
        innslag.setId(i.getId());
        innslag.setGyldigTil(i.getGyldigTil());
        innslag.setSaksnr(i.getSaksnr());
        innslag.setAktiv(i.isAktiv());
        LOG.info("Mappet til innslag {}", innslag);
        return innslag;
    }
}
