package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.historikk.tjenester.dittnav.DoneDTO;
import no.nav.foreldrepenger.historikk.tjenester.dittnav.OppgaveDTO;

public final class MinidialogMapper {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogMapper.class);

    private MinidialogMapper() {

    }

    static JPAMinidialogInnslag fraHendelse(MinidialogHendelse m) {
        LOG.info("Mapper fra {}", m);
        var dialog = new JPAMinidialogInnslag();
        dialog.setYtelseType(m.getYtelseType());
        dialog.setInnsendt(m.getOpprettet());
        dialog.setAktørId(m.getAktørId());
        dialog.setSaksnr(m.getSaksnummer());
        dialog.setGyldigTil(m.getGyldigTil());
        dialog.setAktiv(true);
        dialog.setTekst("tekst");
        dialog.setHendelse(m.getHendelse());
        dialog.setDialogId(m.getDialogId());
        dialog.setJournalpostId(m.getJournalpostId());
        LOG.info("Mappet til {}", dialog);
        return dialog;
    }

    static MinidialogInnslag tilInnslag(JPAMinidialogInnslag i) {
        LOG.info("Mapper fra innslag {}", i);
        var innslag = new MinidialogInnslag(i.getHendelse(), i.getGyldigTil(), i.getTekst(), i.getYtelseType());
        innslag.setAktørId(i.getAktørId());
        innslag.setOpprettet(i.getOpprettet());
        innslag.setSaksnr(i.getSaksnr());
        innslag.setAktiv(i.isAktiv());
        innslag.setDialogId(i.getDialogId());
        innslag.setInnsendt(i.getInnsendt());

        innslag.setJournalpostId(i.getJournalpostId());
        LOG.info("Mappet til innslag {}", innslag);
        return innslag;
    }

    public static OppgaveDTO tilOppgaveDTO(MinidialogHendelse h, String url) {
        return new OppgaveDTO(h.getFnr().getFnr(), h.getSaksnummer(), 3, url, "minidialog TODO",
                h.getDialogId());
    }

    public static DoneDTO tilDoneDTO(MinidialogHendelse h) {
        return new DoneDTO(h.getFnr().getFnr(), h.getSaksnummer(), h.getDialogId());
    }
}
