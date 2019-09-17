package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogController.MINIDIALOG;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;

import java.util.List;

import javax.validation.Valid;

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
@Profile({ LOCAL, DEV })
@RequestMapping(value = MINIDIALOG + "/preprod")
@Unprotected
public class MinidialogDevController {
    private final MinidialogTjeneste minidialog;
    private final MinidialogHendelseProdusent produsent;

    MinidialogDevController(MinidialogTjeneste minidialog, MinidialogHendelseProdusent produsent) {
        this.minidialog = minidialog;
        this.produsent = produsent;
    }

    @PostMapping("/sendMinidialog")
    public void sendMinidialog(@RequestBody MinidialogHendelse hendelse) {
        produsent.sendMinidialogHendelse(hendelse);
    }

    @GetMapping("/minidialoger")
    public List<MinidialogHendelse> hentMinidialoger(@RequestParam("fnr") Fødselsnummer fnr) {
        return minidialog.hentAktiveDialoger(fnr);
    }

    @PostMapping("/lagreMinidialog")
    public void lagreMinidialog(@RequestBody @Valid MinidialogHendelse hendelse) {
        minidialog.lagre(hendelse);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[minidialog=" + minidialog + ", produsent=" + produsent + "]";
    }
}
