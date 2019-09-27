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

    private MinidialogMapper() {

    }

    static JPAMinidialogInnslag fraHendelse(MinidialogHendelse m, String journalpostId) {
        LOG.info("Mapper fra {}", m);
        JPAMinidialogInnslag dialog = new JPAMinidialogInnslag();
        dialog.setReferanseId(m.getReferanseId());
        dialog.setAktørId(m.getAktørId());
        dialog.setFnr(m.getFnr());
        dialog.setTekst(m.getTekst());
        dialog.setSaksnr(m.getSaksNr());
        dialog.setGyldigTil(m.getGyldigTil());
        dialog.setAktiv(m.isAktiv());
        dialog.setJournalpostId(journalpostId);
        dialog.setHendelse(m.getHendelse());
        LOG.info("Mappet til {}", dialog);
        return dialog;
    }

    static Journalpost journalpost(MinidialogHendelse innslag, byte[] dokument) {
        return new Journalpost(UTGAAENDE,
                new AvsenderMottaker(innslag.getFnr(), innslag.getNavn()),
                new Bruker(innslag.getFnr()), null,
                innslag.getTekst(),
                new Sak(innslag.getSaksNr()),
                new Dokument(new DokumentVariant(dokument)));
    }

    static MinidialogInnslag tilInnslag(JPAMinidialogInnslag i) {
        LOG.info("Mapper fra innslag {}", i);
        MinidialogInnslag innslag = new MinidialogInnslag(i.getFnr(), i.getHendelse(), i.getGyldigTil(),
                i.getJournalpostId(), i.getTekst());
        innslag.setAktørId(i.getAktørId());
        innslag.setOpprettet(i.getOpprettet());
        innslag.setSaksnr(i.getSaksnr());
        innslag.setAktiv(i.isAktiv());
        innslag.setReferanseId(i.getReferanseId());
        LOG.info("Mappet til innslag {}", innslag);
        return innslag;
    }
}
