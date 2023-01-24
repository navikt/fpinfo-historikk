package no.nav.foreldrepenger.historikk.tjenester.tidslinje;

import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag;
import no.nav.foreldrepenger.historikk.tjenester.innsending.Innsending;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.Inntektsmelding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Stream.concat;

@ConditionalOnNotProd
@Service
public class TidslinjeTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(TidslinjeTjeneste.class);

    private final Inntektsmelding inntektsmelding;
    private final Innsending innsending;

    private final ArkivTjeneste arkivTjeneste;

    public TidslinjeTjeneste(Innsending innsending,
                             Inntektsmelding inntektsmelding,
                             ArkivTjeneste arkivTjeneste) {
        this.innsending = innsending;
        this.inntektsmelding = inntektsmelding;
        this.arkivTjeneste = arkivTjeneste;
    }

    public List<TidslinjeHendelse> tidslinje(String saksnummer) {
        var dokumenter = arkivTjeneste.hentDokumentoversikt();
        var innslag = concat(inntektsmelding.inntektsmeldinger().stream(),
            innsending.innsendinger().stream())
            .peek(this::loggManglendeSaksnummer)
            .filter(i -> saksnummer.equals(i.getSaksnr()))
            .toList();
        return TidslinjeMapper.map(innslag, dokumenter);
    }

    private void loggManglendeSaksnummer(HistorikkInnslag h) {
        if (h.getSaksnr() == null) {
            LOG.info("Historikkinnslag {} uten saksnummer", h.getReferanseId());
        }
    }


}
