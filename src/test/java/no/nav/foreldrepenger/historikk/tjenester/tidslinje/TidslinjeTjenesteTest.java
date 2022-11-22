package no.nav.foreldrepenger.historikk.tjenester.tidslinje;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivDokument;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;
import no.nav.foreldrepenger.historikk.tjenester.innsending.Innsending;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.ArbeidsgiverInnslag;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.Inntektsmelding;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingInnslag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivDokument.DokumentType.INNGÅENDE_DOKUMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TidslinjeTjeneste.class)
class TidslinjeTjenesteTest {
    private static final AtomicInteger teller = new AtomicInteger();

    @MockBean
    private Innsending innsending;
    @MockBean
    private Inntektsmelding inntektsmeldinger;
    @MockBean
    private ArkivTjeneste arkivTjeneste;

    @Autowired
    private TidslinjeTjeneste tidslinjeTjeneste;

    @Test
    public void innslagSkalMappesTilHendelser() {
        var førstegangssøknadInnslag = innsendingInnslag(HendelseType.INITIELL_FORELDREPENGER);
        var endringssøknadInnslag = innsendingInnslag(HendelseType.ENDRING_FORELDREPENGER);
        var inntektsmeldingInnslag = im(HendelseType.INNTEKTSMELDING_NY);

        when(innsending.innsendinger()).thenReturn(List.of(endringssøknadInnslag, førstegangssøknadInnslag));
        when(inntektsmeldinger.inntektsmeldinger()).thenReturn(List.of(inntektsmeldingInnslag));
        var tidslinje = tidslinjeTjeneste.tidslinje("1234");

        assertThat(tidslinje.size()).isEqualTo(3);
        assertTrue(tidslinje.get(0) instanceof Søknadshendelse);
        assertTrue(tidslinje.get(1) instanceof Søknadshendelse);
        assertTrue(tidslinje.get(2) instanceof InntektsmeldingHendelse);

        assertThat(tidslinje.get(0).getAktørType()).isEqualTo(AktørType.BRUKER);
        assertThat(tidslinje.get(1).getTidslinjeHendelseType()).isEqualTo(TidslinjeHendelseType.ENDRINGSSØKNAD);
        var inntektsmeldingHendelse = (InntektsmeldingHendelse) tidslinje.get(2);
        assertEquals(inntektsmeldingHendelse.getArbeidsgiver().id(), inntektsmeldingInnslag.getArbeidsgiver().getId());
    }

    @Test
    public void hendelserSkalBerikesMedDokumenterFraArkiv() {
        var førstegangssøknadInnslag = innsendingInnslag(HendelseType.INITIELL_FORELDREPENGER);
        var endringssøknadInnslag = innsendingInnslag(HendelseType.ENDRING_FORELDREPENGER);
        when(innsending.innsendinger()).thenReturn(List.of(endringssøknadInnslag, førstegangssøknadInnslag));
        var relevantDokument = new ArkivDokument(INNGÅENDE_DOKUMENT, førstegangssøknadInnslag.getInnsendt(),
            førstegangssøknadInnslag.getSaksnr(), "Eksempeltittel", førstegangssøknadInnslag.getJournalpostId(), null);
        var irrelevantDokument = new ArkivDokument(INNGÅENDE_DOKUMENT, førstegangssøknadInnslag.getInnsendt(),
            førstegangssøknadInnslag.getSaksnr(), "Eksempeltittel", "-1", null);
        when(arkivTjeneste.hentDokumentoversikt()).thenReturn(List.of(relevantDokument, irrelevantDokument));

        var tidslinje = tidslinjeTjeneste.tidslinje("1234");

        assertThat(tidslinje.get(0).getDokumenter()).containsExactly(relevantDokument);
        assertThat(tidslinje.get(1).getDokumenter()).isEmpty();
    }

    private static InntektsmeldingInnslag im(HendelseType type) {
        var verdi = teller.getAndIncrement();
        var nyIm = new InntektsmeldingInnslag();
        nyIm.setHendelseType(type);
        nyIm.setArbeidsgiver(new ArbeidsgiverInnslag("4283478834", "Arbeidsgiver A/S"));
        nyIm.setJournalpostId(String.valueOf(verdi));
        nyIm.setInnsendt(LocalDateTime.now().plusDays(verdi));
        nyIm.setAktørId(AktørId.valueOf("42"));
        nyIm.setSaksnr("1234");
        nyIm.setOpprettet(LocalDateTime.now().plusDays(verdi));
        return nyIm;
    }

    private static InnsendingInnslag innsendingInnslag(HendelseType hendelseType) {
        var verdi = teller.getAndIncrement();
        var tidspunkt = LocalDateTime.now().plusDays(verdi);
        var innsendingInnslag = new InnsendingInnslag(hendelseType);
        innsendingInnslag.setBehandlingsdato(LocalDate.now());
        innsendingInnslag.setInnsendt(tidspunkt);
        innsendingInnslag.setOpprettet(tidspunkt);
        innsendingInnslag.setAktørId(AktørId.valueOf("42"));
        innsendingInnslag.setFnr(Fødselsnummer.valueOf("42"));
        innsendingInnslag.setSaksnr("1234");
        innsendingInnslag.setJournalpostId(String.valueOf(verdi));
        return innsendingInnslag;
    }


}
