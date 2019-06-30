package no.nav.foreldrepenger.historikk.tjenester.historikk;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingEvent;
import no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogEventProdusent;
import no.nav.security.oidc.api.Unprotected;

@RestController
@Profile({ DEV, PREPROD })
@RequestMapping(value = "/historikk/preprod")
@Unprotected
@Api(value = "Endpoint for message management", protocols = "http,https")
public class HistorikkPreprodController {
    private final MinidialogEventProdusent produsent;
    private final HistorikkTjeneste historikk;

    HistorikkPreprodController(MinidialogEventProdusent produsent,
            HistorikkTjeneste historikk) {
        this.produsent = produsent;
        this.historikk = historikk;
    }

    @PostMapping("/historikk/lagre")
    public void lagreHistorikk(InnsendingEvent event) {
        historikk.lagre(event);
    }

    @GetMapping("/historikk/hent")
    public List<HistorikkInnslag> hentHistorikk(@RequestParam("fnr") Fødselsnummer fnr) {
        return historikk.hentHistorikk(fnr);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [produsent=" + produsent + "]";
    }
}
