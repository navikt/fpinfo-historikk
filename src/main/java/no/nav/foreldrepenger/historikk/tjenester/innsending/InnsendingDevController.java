package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.Api;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.http.UnprotectedRestController;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkController;

@UnprotectedRestController(InnsendingDevController.DEVPATH)
@Api(description = "Send og hent innsendingshendelser, kun for testing lokalt og i dev")
public class InnsendingDevController {
    static final String DEVPATH = HistorikkController.HISTORIKK + "/dev";
    private final Innsending innsending;
    private final InnsendingHendelseProdusent produsent;

    InnsendingDevController(Innsending innsending, InnsendingHendelseProdusent produsent) {
        this.innsending = innsending;
        this.produsent = produsent;
    }

    @PostMapping("/lagre")
    public ResponseEntity<?> lagreSøknad(@RequestBody InnsendingHendelse hendelse) {
        innsending.lagreEllerOppdater(hendelse);
        return status(CREATED).build();
    }

    @PostMapping("/send")
    public void produserSøknad(@RequestBody InnsendingHendelse hendelse) {
        produsent.send(hendelse);
    }

    @GetMapping("/søknader")
    public List<InnsendingInnslag> søknader(@RequestParam("aktørId") AktørId id) {
        return innsending.innsendinger(id);
    }

    @GetMapping("/manglendevedlegg")
    public List<String> vedlegg(@RequestParam("saksnummer") String saksnummer,
            @RequestParam("fnr") Fødselsnummer fnr) {
        return innsending.vedleggsInfo(fnr, saksnummer).getManglende();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[innsending=" + innsending + "]";
    }

}
