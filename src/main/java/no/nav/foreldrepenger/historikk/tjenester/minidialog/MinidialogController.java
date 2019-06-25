package no.nav.foreldrepenger.historikk.tjenester.minidialog;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.nav.foreldrepenger.historikk.tjenester.journalføring.ArkivsakSystem;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.AvsenderMottaker;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.Behandlingstema;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.Bruker;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.BrukerIdType;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.IdType;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.JournalføringTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.Journalpost;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.JournalpostType;
import no.nav.foreldrepenger.historikk.tjenester.journalføring.Sak;
import no.nav.foreldrepenger.historikk.util.EnvUtil;
import no.nav.foreldrepenger.historikk.util.MDCUtil;
import no.nav.security.oidc.api.ProtectedWithClaims;

@RestController
@RequestMapping(value = "/minidialog")
@ProtectedWithClaims(issuer = "selvbetjening", claimMap = { "acr=Level4" })
public class MinidialogController implements EnvironmentAware {

    private static final Logger LOG = LoggerFactory.getLogger(MinidialogController.class);
    private final MinidialogTjeneste minidialog;
    private final JournalføringTjeneste journalføring;
    private Environment env;

    MinidialogController(MinidialogTjeneste minidialog, JournalføringTjeneste journalføring) {
        this.minidialog = minidialog;
        this.journalføring = journalføring;
    }

    @GetMapping("/me")
    public List<MinidialogInnslag> hentMinidialog() {
        if (EnvUtil.isPreprod(env)) {
            try {
                journalføring.journalfør(
                        new Journalpost(JournalpostType.INNGAAENDE,
                                new AvsenderMottaker("03016536325", IdType.FNR, "test"),
                                new Bruker(BrukerIdType.FNR, "03016536325"), Behandlingstema.FORELDREPENGER_VED_FØDSEL,
                                "tittel",
                                "NAV", MDCUtil.callId(), Collections.emptyList(), new Sak("42", ArkivsakSystem.GSAK),
                                Collections.emptyList()),
                        false);
            } catch (Exception e) {
                LOG.warn("OOPS", e);
            }
        }
        return minidialog.hentMineAktiveDialoger();
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [minidialog=" + minidialog + "]";
    }
}
