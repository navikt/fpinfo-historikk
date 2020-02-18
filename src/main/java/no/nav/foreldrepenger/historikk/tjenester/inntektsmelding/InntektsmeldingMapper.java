package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType.INNTEKTSMELDING_ENDRING;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType.INNTEKTSMELDING_NY;
import static no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingType.ENDRING;
import static no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingType.NY;
import static no.nav.foreldrepenger.historikk.util.StreamUtil.safeStream;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.Oppslag;

@Component
final class InntektsmeldingMapper {

    private static final Logger LOG = LoggerFactory.getLogger(InntektsmeldingMapper.class);
    private final Oppslag oppslag;

    public InntektsmeldingMapper(Oppslag oppslag) {
        this.oppslag = oppslag;
    }

    public static JPAInntektsmeldingInnslag fraHendelse(InntektsmeldingHendelse hendelse) {
        LOG.info("Mapper fra hendelse {}", hendelse);
        var im = new JPAInntektsmeldingInnslag();
        im.setReferanseId(hendelse.getReferanseId());
        im.setAktørId(hendelse.getAktørId());
        im.setJournalpostId(hendelse.getJournalpostId());
        im.setSaksnr(hendelse.getSaksnummer());
        im.setArbeidsgiver(hendelse.getArbeidsgiverId());
        im.setVersjon(hendelse.getVersjon());
        im.setInnsendt(hendelse.getOpprettet());
        im.setStartDato(hendelse.getStartDato());
        im.setType(hendelse.getHendelse().equals(INNTEKTSMELDING_NY) ? NY : ENDRING);
        LOG.info("Mappet til inntektsmelding {}", im);
        return im;
    }

    List<InntektsmeldingInnslag> tilInnslag(List<JPAInntektsmeldingInnslag> innslag) {
        return safeStream(innslag)
                .map(this::tilInnslag)
                .collect(toList());
    }

    private InntektsmeldingInnslag tilInnslag(JPAInntektsmeldingInnslag i) {
        LOG.info("Mapper fra inntektsmelding {}", i);
        var innslag = new InntektsmeldingInnslag();
        innslag.setOpprettet(i.getOpprettet());
        innslag.setJournalpostId(i.getJournalpostId());
        innslag.setSaksnr(i.getSaksnr());
        innslag.setAktørId(i.getAktørId());
        innslag.setArbeidsgiver(tilArbeidsgiverInnslag(i.getArbeidsgiver()));
        innslag.setReferanseId(i.getReferanseId());
        innslag.setVersjon(i.getVersjon());
        innslag.setInnsendt(i.getInnsendt());
        innslag.setHendelseType(i.getType().equals(NY) ? INNTEKTSMELDING_NY : INNTEKTSMELDING_ENDRING);
        innslag.setStartDato(i.getStartDato());
        LOG.info("Mappet til inntektsmelding {}", innslag);
        return innslag;
    }

    private ArbeidsgiverInnslag tilArbeidsgiverInnslag(String arbeidsgiver) {
        if (arbeidsgiver == null) {
            return new ArbeidsgiverInnslag(arbeidsgiver, null);
        }

        return new ArbeidsgiverInnslag(arbeidsgiver, arbeidsgiverNavn(arbeidsgiver));
    }

    private String arbeidsgiverNavn(String arbeidsgiver) {
        if (arbeidsgiver == null) {
            return null;
        }
        if (arbeidsgiver.length() == 9) {
            return oppslag.orgNavn(arbeidsgiver);
        }

        if (arbeidsgiver.length() == 13) {
            return oppslag.personNavn(AktørId.valueOf(arbeidsgiver));
        }
        LOG.warn("Arbeidsgiver {} kan ikke håndteres", arbeidsgiver);
        return null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[oppslag=" + oppslag + "]";
    }
}
