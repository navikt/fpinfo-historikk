package no.nav.foreldrepenger.historikk.meldinger.minidialog;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.MinidialogInnslag;
import no.nav.foreldrepenger.historikk.meldinger.innsending.SøknadType;
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
    public List<MinidialogInnslag> hentAktiveDialogerForAktør(@RequestParam("aktørId") AktørId aktørId) {
        return minidialog.hentAktiveDialogerForAktør(aktørId);
    }

    @PostMapping("/produser")
    public void produser(MinidialogInnslag hendelse) {
        produsent.sendMinidialogHendelse(hendelse);
    }

    @PostMapping("/merk")
    public int deaktiverMinidialoger(AktørId aktørId, SøknadType type) {
        return minidialog.deaktiver(aktørId.getAktørId(), type);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [minidialog=" + minidialog + "]";
    }
}
