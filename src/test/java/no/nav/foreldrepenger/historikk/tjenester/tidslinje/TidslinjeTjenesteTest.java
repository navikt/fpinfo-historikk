package no.nav.foreldrepenger.historikk.tjenester.tidslinje;

import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivTjeneste;
import no.nav.foreldrepenger.historikk.tjenester.innsending.Innsending;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.Inntektsmelding;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.Oppslag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TidslinjeTjeneste.class)
class TidslinjeTjenesteTest {
    @Autowired
    TidslinjeTjeneste tidslinjeTjeneste;

    @MockBean
    Innsending innsending;

    @MockBean
    Inntektsmelding inntektsmelding;

    @MockBean
    ArkivTjeneste arkivTjeneste;

    @MockBean
    Oppslag oppslag;

    @Test
    public void testAtFeilIkkePropagerer() {
        when(inntektsmelding.inntektsmeldinger(any())).thenThrow(IllegalStateException.class);
        assertDoesNotThrow(() -> tidslinjeTjeneste.testTidslinje("42"));
    }

}
