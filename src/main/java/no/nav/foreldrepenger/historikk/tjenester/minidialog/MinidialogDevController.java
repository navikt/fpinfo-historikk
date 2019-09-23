package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import static no.nav.foreldrepenger.historikk.tjenester.minidialog.MinidialogController.MINIDIALOG;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
@RequestMapping(path = MinidialogDevController.DEVPATH, produces = APPLICATION_JSON_VALUE)
@Unprotected
public class MinidialogDevController {
    static final String DEVPATH = MINIDIALOG + "/dev";
    private final MinidialogTjeneste minidialog;
    private final MinidialogHendelseProdusent produsent;

    MinidialogDevController(MinidialogTjeneste minidialog, MinidialogHendelseProdusent produsent) {
        this.minidialog = minidialog;
        this.produsent = produsent;
    }

    @PostMapping("/sendMinidialog")
    public void sendMinidialog(@RequestBody MinidialogHendelse hendelse) {
        produsent.sendHendelse(hendelse);
    }

    @GetMapping("/minidialoger")
    public List<MinidialogInnslag> minidialoger(@RequestParam("fnr") Fødselsnummer fnr,
            @RequestParam(name = "activeOnly", defaultValue = "true") boolean activeOnly) {
        return minidialog.hentDialoger(fnr, activeOnly);
    }

    @GetMapping("/spm")
    public List<MinidialogInnslag> spørsmål(@RequestParam("fnr") Fødselsnummer fnr) {
        return minidialog.hentAktiveDialogSpørsmål(fnr);
    }

    @PostMapping("/lagreMinidialog")
    public void lagreMinidialog(@RequestBody @Valid MinidialogHendelse hendelse) {
        minidialog.lagre(hendelse, "42");
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[minidialog=" + minidialog + ", produsent=" + produsent + "]";
    }
}
