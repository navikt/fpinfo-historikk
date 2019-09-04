package no.nav.foreldrepenger.historikk.tjenester.inntektsmelding;

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
        LOG.info("Mappet til inntektsmelding {}", inntektsmelding);
        return inntektsmelding;
    }
}
