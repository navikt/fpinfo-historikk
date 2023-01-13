package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static java.util.Collections.emptyList;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag.SORT_OPPRETTET_ASC;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingMapper.nyFra;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingMapper.oppdaterFra;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingMapper.tilInnslag;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.JPAInnsendingSpec.harAktørId;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.nav.foreldrepenger.common.util.TokenUtil;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.Oppslag;

@Service
public class Innsending {

    private static final Logger LOG = LoggerFactory.getLogger(Innsending.class);

    private final JPAInnsendingRepository dao;
    private final Oppslag oppslag;
    private final TokenUtil tokenUtil;

    public Innsending(JPAInnsendingRepository dao, Oppslag oppslag, TokenUtil tokenUtil) {
        this.dao = dao;
        this.oppslag = oppslag;
        this.tokenUtil = tokenUtil;
    }

    public void lagreEllerOppdater(InnsendingHendelse h) {
        var eksisterende = dao.findByReferanseId(h.getReferanseId());
        if (eksisterende != null) {
            LOG.debug("Fant et eksisterende innsendingsinnslag i innsending {}", eksisterende);
            var oppdatert = oppdaterFra(h, eksisterende);
            dao.save(oppdatert);
            LOG.debug("Oppdaterte innsendingsinnslag OK");
        } else {
            LOG.debug("Ingenting å oppdatere fra innsendingshendelse, insert istedet");
            var ny = nyFra(h);
            dao.save(ny);
            LOG.debug("Insert fra innsendingshendelse OK");
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

    public VedleggsInfo vedleggsInfo(String saksnummer) {
        var fnr = tokenUtil.autentisertBrukerOrElseThrowException();
        return vedleggsInfo(Fødselsnummer.valueOf(fnr.value()), saksnummer);
    }

    public VedleggsInfo vedleggsInfo(Fødselsnummer fnr, String saksnummer) {
        return vedleggsInfo(fnr, saksnummer, null);
    }

    public VedleggsInfo vedleggsInfo(Fødselsnummer fnr, String saksnummer, String currentRef) {
        return vedleggsInfo(hendelserForFnrAndSaksnr(saksnummer, fnr), currentRef);
    }

    private static VedleggsInfo vedleggsInfo(List<InnsendingInnslag> innslag, String currentRef) {
        try {
            var manglende = new ArrayList<String>();
            var innsendte = new ArrayList<String>();
            for (var hendelse : innslag) {
                LOG.trace("Tidligere innsending er {} ", hendelse);
                if (!Objects.equals(hendelse.getReferanseId(), currentRef) && !hendelse.getVedlegg().isEmpty()) {
                    innsendte.add(hendelse.getReferanseId());
                }
                if (hendelse.getHendelse().erInitiell()) {
                    LOG.trace("Ny førstegangsinnsending, fjerner gamle manglende vedlegg");
                    manglende.clear();
                }
                if (!hendelse.getIkkeOpplastedeVedlegg().isEmpty()) {
                    LOG.trace("Legger til {} i {}", hendelse.getIkkeOpplastedeVedlegg(), manglende);
                    manglende.addAll(hendelse.getIkkeOpplastedeVedlegg());
                }
                LOG.trace("Fjerner {} fra {}", hendelse.getOpplastedeVedlegg(), manglende);
                hendelse.getOpplastedeVedlegg().forEach(manglende::remove);
                LOG.trace("Ikke-opplastede etter fjerning er {}", manglende);
            }
            if (!manglende.isEmpty()) {
                LOG.info("Ikke-opplastede vedlegg er {}", manglende);
            }
            return new VedleggsInfo(innsendte, manglende);
        } catch (Exception e) {
            LOG.warn("Kunne ikke hente tidligere innsendinger", e);
            return VedleggsInfo.NONE;
        }
    }

    private List<InnsendingInnslag> hendelserForFnrAndSaksnr(String saksnr, Fødselsnummer fnr) {
        return Optional.ofNullable(saksnr)
                .map(s -> dao.findBySaksnrAndFnrOrderByOpprettetAsc(s, fnr))
                .map(InnsendingMapper::tilInnslag)
                .orElse(emptyList());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[dao=" + dao + ", oppslag=" + oppslag + "]";
    }

}
