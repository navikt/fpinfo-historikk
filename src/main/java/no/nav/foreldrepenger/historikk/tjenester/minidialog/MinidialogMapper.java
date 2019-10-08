package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;

public final class MinidialogMapper {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogMapper.class);

    private MinidialogMapper() {

    }

    static JPAMinidialogInnslag fraHendelse(MinidialogHendelse m, Fødselsnummer fnr) {
        LOG.info("Mapper fra {}", m);
        var dialog = new JPAMinidialogInnslag();
        dialog.setReferanseId(m.getReferanseId());
        dialog.setAktørId(m.getAktørId());
        dialog.setFnr(fnr);
        dialog.setTekst(m.getTekst());
        dialog.setSaksnr(m.getSaksNr());
        dialog.setGyldigTil(m.getGyldigTil());
        dialog.setAktiv(true);
        dialog.setHendelse(m.getHendelseType());
        dialog.setReferanseId(m.getReferanseId());
        LOG.info("Mappet til {}", dialog);
        return dialog;
    }

    static MinidialogInnslag tilInnslag(JPAMinidialogInnslag i) {
        LOG.info("Mapper fra innslag {}", i);
        var innslag = new MinidialogInnslag(i.getFnr(), i.getHendelse(), i.getGyldigTil(), i.getTekst());
        innslag.setAktørId(i.getAktørId());
        innslag.setOpprettet(i.getOpprettet());
        innslag.setSaksnr(i.getSaksnr());
        innslag.setAktiv(i.isAktiv());
        innslag.setReferanseId(i.getReferanseId());
        LOG.info("Mappet til innslag {}", innslag);
        return innslag;
    }
}
