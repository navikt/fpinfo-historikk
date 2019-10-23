package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.util.StreamUtil.safeStream;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
        im.setJournalpostId(hendelse.getJournalId());
        im.setSaksnr(hendelse.getSaksnummer());
        im.setArbeidsgiver(hendelse.getArbeidsgiver());
        im.setVersjon(hendelse.getVersjon());
        im.setInnsendingsTidspunkt(hendelse.getInnsendingsTidspunkt());
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
        innslag.setType(i.getType());
        LOG.info("Mappet til inntektsmelding {}", innslag);
        return innslag;
    }

    private ArbeidsgiverInnslag tilArbeidsgiverInnslag(String arbeidsgiver) {
        if (arbeidsgiver == null) {
            return new ArbeidsgiverInnslag(arbeidsgiver, null);
        }
        if (arbeidsgiver.length() == 9) {
            new ArbeidsgiverInnslag(arbeidsgiver, oppslag.orgNavn(arbeidsgiver));
        }

        if (arbeidsgiver.length() == 11) {
            new ArbeidsgiverInnslag(arbeidsgiver, "Privat arbeidsgiver");
        }
        return new ArbeidsgiverInnslag(arbeidsgiver, null);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[oppslag=" + oppslag + "]";
    }
}
