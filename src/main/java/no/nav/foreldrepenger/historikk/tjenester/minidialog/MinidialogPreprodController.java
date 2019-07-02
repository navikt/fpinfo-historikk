package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogController.MINIDIALOG;
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
@RequestMapping(value = MINIDIALOG + "/preprod")
@Unprotected
public class MinidialogPreprodController {
    private final MinidialogTjeneste minidialog;
    private final MinidialogHendelseProdusent produsent;

    MinidialogPreprodController(MinidialogTjeneste minidialog, MinidialogHendelseProdusent produsent) {
        this.minidialog = minidialog;
        this.produsent = produsent;
    }

    @GetMapping("/aktive")
    public List<MinidialogHendelse> hentAktiveDialogerForFnr(@RequestParam("fnr") Fødselsnummer fnr) {
        return minidialog.hentAktiveDialoger(fnr);
    }

    @PostMapping("/produser")
    public void produser(@RequestBody MinidialogHendelse hendelse) {
        produsent.sendMinidialogHendelse(hendelse);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [minidialog=" + minidialog + "]";
    }
}
