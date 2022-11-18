package no.nav.foreldrepenger.historikk.tjenester.tidslinje;
import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.common.innsyn.v2.Arbeidsgiver;
import no.nav.foreldrepenger.historikk.http.ProtectedRestController;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkController;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag;
import no.nav.foreldrepenger.historikk.tjenester.innsending.Innsending;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.Inntektsmelding;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingInnslag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Stream.concat;
import static no.nav.foreldrepenger.historikk.tjenester.tidslinje.TidslinjeController.PATH;

@ConditionalOnNotProd
@ProtectedRestController(PATH)
public class TidslinjeController {

    public static final String PATH = HistorikkController.HISTORIKK + "/tidslinje";
    private static final Logger LOG = LoggerFactory.getLogger(TidslinjeController.class);
    private final TidslinjeTjeneste tidslinjeTjeneste;

    public TidslinjeController(TidslinjeTjeneste tidslinjeTjeneste) {
        this.tidslinjeTjeneste = tidslinjeTjeneste;
    }


    @GetMapping("/{saksnummer}")
    public List<TidslinjeHendelse> tidslinje(String saksnummer) {
        return tidslinjeTjeneste.tidslinje(saksnummer);
    }

}
