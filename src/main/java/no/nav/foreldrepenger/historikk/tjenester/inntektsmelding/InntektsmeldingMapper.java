package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.dao.JPAInntektsmelding;

public final class InntektsmeldingMapper {

    private static final Logger LOG = LoggerFactory.getLogger(InntektsmeldingMapper.class);

    private InntektsmeldingMapper() {

    }

    public static JPAInntektsmelding fraInntektsmeldingHendelse(InntektsmeldingHendelse hendelse) {
        LOG.info("Mapper fra hendelse {}", hendelse);
        JPAInntektsmelding inntektsmelding = new JPAInntektsmelding();
        inntektsmelding.setAktørId(hendelse.getAktørId());
        inntektsmelding.setFnr(hendelse.getFnr());
        inntektsmelding.setJournalpostId(hendelse.getJournalId());
        inntektsmelding.setSaksnr(hendelse.getSaksNr());
        inntektsmelding.setArbeidsgiver(hendelse.getArbeidsgiver());
        LOG.info("Mappet til inntektsmelding {}", inntektsmelding);
        return inntektsmelding;
    }

    static InntektsmeldingHistorikkInnslag tilHistorikkInnslag(JPAInntektsmelding i) {
        LOG.info("Mapper fra inntektsmelding {}", i);
        InntektsmeldingHistorikkInnslag innslag = new InntektsmeldingHistorikkInnslag(i.getFnr());
        innslag.setOpprettet(i.getOpprettet());
        innslag.setJournalpostId(i.getJournalpostId());
        innslag.setSaksnr(i.getSaksnr());
        innslag.setAktørId(i.getAktørId());
        innslag.setArbeidsgiver(i.getArbeidsgiver());
        LOG.info("Mappet til inntektsmelding {}", innslag);
        return innslag;
    }

    public static List<InntektsmeldingHistorikkInnslag> konverterFra(List<JPAInntektsmelding> innslag) {
        return innslag
                .stream()
                .map(InntektsmeldingMapper::tilHistorikkInnslag)
                .collect(toList());
    }
}
