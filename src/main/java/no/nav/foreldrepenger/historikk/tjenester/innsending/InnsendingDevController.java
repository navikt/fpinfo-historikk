package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static no.nav.foreldrepenger.historikk.util.EnvUtil.DEV;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.LOCAL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkController;
import no.nav.security.oidc.api.Unprotected;

@RestController
@Profile({ LOCAL, DEV })
@RequestMapping(path = InnsendingDevController.DEVPATH, produces = APPLICATION_JSON_VALUE)
@Unprotected
public class InnsendingDevController {
    static final String DEVPATH = HistorikkController.HISTORIKK + "/dev";
    private final InnsendingHendelseProdusent produsent;
    private final InnsendingTjeneste innsending;

    InnsendingDevController(InnsendingHendelseProdusent produsent, InnsendingTjeneste innsending) {
        this.produsent = produsent;
        this.innsending = innsending;
    }

    @PostMapping("/send")
    public void produserSøknad(@RequestBody InnsendingInnsendingHendelse hendelse) {
        produsent.sendHendelse(hendelse);
    }

    @PostMapping("/lagre")
    public void lagreSøknad(@RequestBody InnsendingInnsendingHendelse hendelse) {
        innsending.lagre(hendelse);
    }

    @GetMapping("/søknader")
    public List<InnsendingInnslag> søknader(@RequestParam("fnr") Fødselsnummer fnr) {
        return innsending.hentInnsendinger(fnr);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[produsent=" + produsent + ", innsending=" + innsending + "]";
    }

}
