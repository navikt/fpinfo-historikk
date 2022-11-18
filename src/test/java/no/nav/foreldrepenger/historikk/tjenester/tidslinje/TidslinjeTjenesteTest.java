package no.nav.foreldrepenger.historikk.tjenester.tidslinje;

import no.nav.foreldrepenger.common.util.TokenUtil;
import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag;
import no.nav.foreldrepenger.historikk.tjenester.innsending.Innsending;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.ArbeidsgiverInnslag;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.Inntektsmelding;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.Oppslag;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TidslinjeTjeneste.class)
class TidslinjeTjenesteTest {

    @MockBean
    private Oppslag oppslag;
    @MockBean
    private TokenUtil tokenUtil;
    @MockBean
    private Innsending innsending;
    @MockBean
    private Inntektsmelding inntektsmeldinger;

    @Autowired
    private TidslinjeTjeneste tidslinjeTjeneste;

    InnsendingInnslag innsendingFp = new InnsendingInnslag(HendelseType.INITIELL_FORELDREPENGER);
    AtomicInteger teller = new AtomicInteger();

    @Test
    public void test() {
        var førstegangssøknad = innsendingInnslag(HendelseType.INITIELL_FORELDREPENGER);
        var endringssøknad = innsendingInnslag(HendelseType.ENDRING_FORELDREPENGER);
        var inntektsmelding = im(HendelseType.INNTEKTSMELDING_NY);

        when(innsending.innsendinger()).thenReturn(List.of(førstegangssøknad, endringssøknad));
        when(inntektsmeldinger.inntektsmeldinger()).thenReturn(List.of(inntektsmelding));
        var hepp = tidslinjeTjeneste.tidslinje("1234");
    }

    private InntektsmeldingInnslag im(HendelseType type) {
        var verdi = teller.getAndIncrement();
        var nyIm = new InntektsmeldingInnslag();
        nyIm.setHendelseType(type);
        nyIm.setArbeidsgiver(new ArbeidsgiverInnslag("4283478834", "Arbeidsgiver A/S"));
        nyIm.setJournalpostId(String.valueOf(verdi));
        nyIm.setInnsendt(LocalDateTime.now().plusDays(verdi));
        nyIm.setAktørId(AktørId.valueOf("42"));
        nyIm.setSaksnr("42");
        return nyIm;
    }

    private InnsendingInnslag innsendingInnslag(HendelseType hendelseType) {
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
