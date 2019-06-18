package no.nav.foreldrepenger.historikk.meldinger;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.PREPROD;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.MinidialogInnslag;
import no.nav.security.oidc.api.Unprotected;

@RestController
@Profile({ DEV, PREPROD })
@RequestMapping(value = "/minidialog/preprod")
@Unprotected
public class MinidialogPreprodController {
    private final MinidialogTjeneste minidialog;

    MinidialogPreprodController(MinidialogTjeneste minidialog) {
        this.minidialog = minidialog;
    }

    @GetMapping("/find")
    public List<MinidialogInnslag> hentMeldingerForAktør(@RequestParam("aktørId") AktørId aktørId) {
        return minidialog.hentAktiveDialogerForAktør(aktørId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [minidialog=" + minidialog + "]";
    }
}
