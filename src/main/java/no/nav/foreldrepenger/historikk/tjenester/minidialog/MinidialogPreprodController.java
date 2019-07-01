package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.security.oidc.api.Unprotected;

@RestController
@Profile({ DEV, PREPROD })
@RequestMapping(value = "/minidialog/preprod")
@Unprotected
public class MinidialogPreprodController {
    private final MinidialogTjeneste minidialog;
    private final MinidialogEventProdusent produsent;

    MinidialogPreprodController(MinidialogTjeneste minidialog, MinidialogEventProdusent produsent) {
        this.minidialog = minidialog;
        this.produsent = produsent;
    }

    @GetMapping("/aktive")
    public List<MinidialogInnslag> hentAktiveDialogerForFnr(@RequestParam("fnr") Fødselsnummer fnr) {
        return minidialog.hentAktiveDialogerForFnr(fnr);
    }

    @PostMapping("/produser")
    public void produser(@RequestBody MinidialogInnslag hendelse) {
        produsent.sendMinidialogHendelse(hendelse);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [minidialog=" + minidialog + "]";
    }
}
