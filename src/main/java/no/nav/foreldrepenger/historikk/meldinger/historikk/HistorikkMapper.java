package no.nav.foreldrepenger.historikk.meldinger.historikk;

import static java.util.stream.Collectors.toList;

import java.util.List;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.HistorikkInnslag;
import no.nav.foreldrepenger.historikk.meldinger.historikk.dao.JPAHistorikkInnslag;
import no.nav.foreldrepenger.historikk.meldinger.innsending.InnsendingEvent;

public final class HistorikkMapper {

    private HistorikkMapper() {

    }

    static HistorikkInnslag tilHistorikkInnslag(JPAHistorikkInnslag i) {
        HistorikkInnslag innslag = new HistorikkInnslag(new AktørId(i.getAktørId()), i.getTekst());
        innslag.setOpprettet(i.getOpprettet());
        innslag.setJournalpostId(i.getJournalpostId());
        innslag.setSaksnr(i.getSaksnr());
        return innslag;
    }

    static JPAHistorikkInnslag fraEvent(InnsendingEvent event) {

        JPAHistorikkInnslag innslag = new JPAHistorikkInnslag(event.getAktørId(), event.getType().name());
        innslag.setSaksnr(event.getSaksNr());
        innslag.setJournalpostId(event.getJournalId());
        innslag.setTekst(event.getType().name());
        return innslag;
    }

    static List<HistorikkInnslag> konverterFra(List<JPAHistorikkInnslag> innslag) {
        return innslag
                .stream()
                .map(HistorikkMapper::tilHistorikkInnslag)
                .collect(toList());
    }
}
