package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.util.StreamUtil.safeStream;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import no.nav.foreldrepenger.historikk.tjenester.oppslag.OppslagTjeneste;

@Component
final class InntektsmeldingMapper {

    private static final Logger LOG = LoggerFactory.getLogger(InntektsmeldingMapper.class);
    private final OppslagTjeneste oppslag;

    public InntektsmeldingMapper(OppslagTjeneste oppslag) {
        this.oppslag = oppslag;
    }

    public static JPAInntektsmeldingInnslag fraHendelse(InntektsmeldingHendelse hendelse) {
        LOG.info("Mapper fra hendelse {}", hendelse);
        var inntektsmelding = new JPAInntektsmeldingInnslag();
        inntektsmelding.setReferanseId(hendelse.getReferanseId());
        inntektsmelding.setAktørId(hendelse.getAktørId());
        inntektsmelding.setJournalpostId(hendelse.getJournalId());
        inntektsmelding.setSaksnr(hendelse.getSaksNr());
        inntektsmelding.setArbeidsgiver(hendelse.getArbeidsgiver());
        LOG.info("Mappet til inntektsmelding {}", inntektsmelding);
        return inntektsmelding;
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
        LOG.info("Mappet til inntektsmelding {}", innslag);
        return innslag;
    }

    private ArbeidsgiverInnslag tilArbeidsgiverInnslag(JPAArbeidsgiver arbeidsgiver) {
        String orgnr = arbeidsgiver.getOrgnr();
        return new ArbeidsgiverInnslag(orgnr, oppslag.orgNavn(orgnr));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[oppslag=" + oppslag + "]";
    }
}
