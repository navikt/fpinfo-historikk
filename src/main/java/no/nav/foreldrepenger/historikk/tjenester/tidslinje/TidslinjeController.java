package no.nav.foreldrepenger.historikk.tjenester.tidslinje;

import no.nav.foreldrepenger.historikk.http.ProtectedRestController;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static no.nav.foreldrepenger.historikk.tjenester.tidslinje.TidslinjeController.PATH;

@ProtectedRestController(PATH)
public class TidslinjeController {
    public static final String PATH = HistorikkController.HISTORIKK + "/tidslinje";
    private final TidslinjeTjeneste tidslinjeTjeneste;

    public TidslinjeController(TidslinjeTjeneste tidslinjeTjeneste) {
        this.tidslinjeTjeneste = tidslinjeTjeneste;
    }


    @GetMapping
    public List<TidslinjeHendelse> tidslinje(@RequestParam String saksnummer) {
        return tidslinjeTjeneste.tidslinje(saksnummer);
    }

}
