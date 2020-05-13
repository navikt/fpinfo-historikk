package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static java.util.Collections.emptyList;
import static no.nav.foreldrepenger.historikk.config.TxConfiguration.JPA_TM;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag.SORT_OPPRETTET_ASC;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingMapper.nyFra;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingMapper.oppdaterFra;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingMapper.tilInnslag;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.JPAInnsendingSpec.harAktørId;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.Oppslag;

@Service
@Transactional(JPA_TM)
public class Innsending {

    private static final Logger LOG = LoggerFactory.getLogger(Innsending.class);

    private final JPAInnsendingRepository dao;
    private final Oppslag oppslag;

    public Innsending(JPAInnsendingRepository dao, Oppslag oppslag) {
        this.dao = dao;
        this.oppslag = oppslag;
    }

    public void lagreEllerOppdater(InnsendingHendelse h) {
        var eksisterende = dao.findByReferanseId(h.getReferanseId());
        if (eksisterende != null) {
            LOG.info("Fant et eksisterende innsendingsinnslag i innsending {}", eksisterende);
            LOG.info("Oppdaterer eksisterende innsendingsinnslag i innsending fra {}", h);
            JPAInnsendingInnslag oppdatert = oppdaterFra(h, eksisterende);
            LOG.info("Oppdaterer innsendingsinnslag  i innsending er {}", oppdatert);
            dao.save(oppdatert);
            LOG.info("Oppdaterte innsendingsinnslag OK");
        } else {
            LOG.info("Ingenting å oppdatere fra innsendingshendelse, insert istedet");
            JPAInnsendingInnslag ny = nyFra(h);
            LOG.info("Nytt innsendingsinnslag  i innsending er {}", ny);
            dao.save(ny);
            LOG.info("Insert fra innsendingshendelse OK");
        }
    }

    public void lagreEllerOppdater(InnsendingFordeltOgJournalførtHendelse h) {
        var eksisterende = dao.findByReferanseId(h.getForsendelseId());
        if (eksisterende != null) {
            LOG.info("fant et eksisterende innsendingsinnslag {}", eksisterende);
            if (eksisterende.getSaksnr() == null && eksisterende.getJournalpostId() == null) {
                LOG.info("Oppdaterer innsendingsinnslag med saksnr og journalpostid");
                JPAInnsendingInnslag oppdatert = oppdaterFra(h, eksisterende);
                LOG.info("Oppdaterer innsendingsinnslag i fordeling er {}", oppdatert);
                dao.save(oppdatert);
                LOG.info("Oppdaterer fra fordelingshendelse OK");
            } else {
                LOG.info("Eksisterende innslag er allerede komplett");
            }
        } else {
            LOG.info("Ingenting å oppdatere fra fordelingshendelse, insert istedet");
            JPAInnsendingInnslag ny = nyFra(h);
            LOG.info("Nytt innsendingsinnslag  i fordeling er {}", ny);
            dao.save(ny);
            LOG.info("Insert fra hendelse OK");
        }
    }

    @Transactional(readOnly = true)
    public List<InnsendingInnslag> innsendinger() {
        return innsendinger(oppslag.aktørId());
    }

    @Transactional(readOnly = true)
    public List<InnsendingInnslag> innsendinger(AktørId id) {
        LOG.info("Henter innsendingsinnslag for {}", id);
        var innslag = tilInnslag(dao.findAll(where(harAktørId(id)), SORT_OPPRETTET_ASC));
        LOG.info("Hentet innsendingsinnslag {}", innslag);
        return innslag;
    }

    public VedleggsInfo vedleggsInfo(Fødselsnummer fnr, String saksnummer) {
        return vedleggsInfo(fnr, saksnummer, null);
    }

    public VedleggsInfo vedleggsInfo(Fødselsnummer fnr, String saksnummer, String currentRef) {
        try {
            var aktørId = oppslag.aktørId();
            LOG.info("AktørID for {} er {}", fnr, aktørId);
            var manglendeDokumentIder = new ArrayList<String>();
            var eventIder = new ArrayList<String>();
            for (var hendelse : hendelserForSaksnrAndAktørId(saksnummer, aktørId)) {
                LOG.trace("Tidligere innsending for {} er {} ", saksnummer, hendelse);
                if (hendelse.getReferanseId() != currentRef && !hendelse.getVedlegg().isEmpty()) {
                    eventIder.add(hendelse.getReferanseId());
                }
                if (hendelse.getHendelse().erInitiell()) {
                    LOG.trace("Ny førstegangsinnsending for {}, fjerner gamle manglende vedlegg", saksnummer);
                    manglendeDokumentIder.clear();
                }
                LOG.trace("Legger til {} i {}", hendelse.ikkeOpplastedeVedlegg(), manglendeDokumentIder);
                manglendeDokumentIder.addAll(hendelse.ikkeOpplastedeVedlegg());
                LOG.trace("Fjerner {} fra {}", hendelse.opplastedeVedlegg(), manglendeDokumentIder);
                hendelse.opplastedeVedlegg().stream().forEach(manglendeDokumentIder::remove);
                LOG.trace("Ikke-opplastede etter fjerning er {}", manglendeDokumentIder);
            }
            LOG.info("Ikke-opplastede vedlegg for {} {}", saksnummer, manglendeDokumentIder);
            return new VedleggsInfo(eventIder, manglendeDokumentIder);
        } catch (Exception e) {
            LOG.warn("Kunne ikke hente tidligere innsendinger", e);
            return VedleggsInfo.NONE;
        }
    }

    private List<InnsendingInnslag> hendelserForSaksnrAndAktørId(String saksnr, AktørId id) {
        return Optional.ofNullable(saksnr)
                .map(dao::findBySaksnrOrderByOpprettetAsc)
                .map(InnsendingMapper::tilInnslag)
                .orElse(emptyList());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dao=" + dao + ", oppslag=" + oppslag + "]";
    }

}
