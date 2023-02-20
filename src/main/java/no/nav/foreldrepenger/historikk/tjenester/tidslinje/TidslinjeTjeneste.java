package no.nav.foreldrepenger.historikk.tjenester.tidslinje;

import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag;
import no.nav.foreldrepenger.historikk.tjenester.innsending.Innsending;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.Inntektsmelding;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.Oppslag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Stream.concat;

@Service
public class TidslinjeTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(TidslinjeTjeneste.class);

    private final Inntektsmelding inntektsmelding;
    private final Innsending innsending;

    private final ArkivTjeneste arkivTjeneste;
    private final Oppslag oppslag;

    public TidslinjeTjeneste(Innsending innsending,
                             Inntektsmelding inntektsmelding,
                             ArkivTjeneste arkivTjeneste,
                             Oppslag oppslag) {
        this.innsending = innsending;
        this.inntektsmelding = inntektsmelding;
        this.arkivTjeneste = arkivTjeneste;
        this.oppslag = oppslag;
    }

    public List<TidslinjeHendelse> tidslinje(String saksnummer) {
        var aktørId = oppslag.aktørId();
        var dokumenter = arkivTjeneste.hentDokumentoversikt().stream()
              .filter(ad -> saksnummer.equals(ad.getSaksnummer()))
              .toList();
        var innslag = concat(inntektsmelding.inntektsmeldinger(aktørId).stream(),
            innsending.innsendinger(aktørId).stream())
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


    public void testTidslinje(String saksnummer) {
        try {
            var tidslinje = tidslinje(saksnummer).stream()
                              .collect(Collectors.groupingBy(
                                  TidslinjeHendelse::getTidslinjeHendelseType, Collectors.counting())
                              );
            LOG.info("Tidslinje: hentet uten feil {}", tidslinje);
        } catch (Exception e) {
            LOG.info("Tidslinje: feil", e);
        }
    }
}
