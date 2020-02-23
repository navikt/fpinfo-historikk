package no.nav.foreldrepenger.historikk.tjenester.vedtak;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.util.StreamUtil.safeStream;
import static no.nav.foreldrepenger.historikk.util.StringUtil.flertall;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.abakus.vedtak.ytelse.v1.YtelseV1;

public final class VedtakMapper {

    private static final Logger LOG = LoggerFactory.getLogger(VedtakMapper.class);

    private VedtakMapper() {

    }

    static JPAVedtakInnslag fraVedtak(YtelseV1 m) {
        LOG.info("Mapper fra {}", m);
        var innslag = new JPAVedtakInnslag();
        LOG.info("Mappet til {}", innslag);
        return innslag;
    }

    static VedtakInnslag tilVedtak(JPAVedtakInnslag i) {
        LOG.info("Mapper fra innslag {}", i);
        var innslag = new VedtakInnslag();
        LOG.info("Mappet til innslag {}", innslag);
        return innslag;
    }

    static List<VedtakInnslag> tilVedtak(List<JPAVedtakInnslag> innslag) {
        var i = safeStream(innslag)
                .map(VedtakMapper::tilVedtak)
                .collect(toList());
        LOG.info("Hentet {} dialog{} ({})", i.size(), flertall(i), i);
        return i;

    }

}
