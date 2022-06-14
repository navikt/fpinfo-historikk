package no.nav.foreldrepenger.historikk.tjenester.tilbakekreving;

public final class TilbakekrevingMapper {

    private TilbakekrevingMapper() {
    }

    static JPATilbakekrevingInnslag fraHendelse(TilbakekrevingHendelse m) {
        var dialog = new JPATilbakekrevingInnslag();
        dialog.setYtelseType(m.getYtelseType());
        dialog.setInnsendt(m.getOpprettet());
        dialog.setAktørId(m.getAktørId());
        dialog.setFnr(m.getFnr());
        dialog.setSaksnr(m.getSaksnummer());
        dialog.setGyldigTil(m.getGyldigTil());
        dialog.setAktiv(m.isAktiv());
        dialog.setTekst("TODO");
        dialog.setHendelse(m.getHendelse());
        dialog.setDialogId(m.getDialogId());
        dialog.setJournalpostId(m.getJournalpostId());
        return dialog;
    }

    static TilbakekrevingInnslag tilInnslag(JPATilbakekrevingInnslag i) {
        var innslag = new TilbakekrevingInnslag(i.getHendelse(), i.getGyldigTil(), i.getTekst(), i.getYtelseType());
        innslag.setAktørId(i.getAktørId());
        innslag.setFnr(i.getFnr());
        innslag.setOpprettet(i.getOpprettet());
        innslag.setSaksnr(i.getSaksnr());
        innslag.setAktiv(i.isAktiv());
        innslag.setDialogId(i.getDialogId());
        innslag.setInnsendt(i.getInnsendt());
        innslag.setJournalpostId(i.getJournalpostId());
        return innslag;
    }

}
