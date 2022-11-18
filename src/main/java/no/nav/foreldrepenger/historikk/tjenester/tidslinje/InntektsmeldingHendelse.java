package no.nav.foreldrepenger.historikk.tjenester.tidslinje;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import no.nav.foreldrepenger.common.innsyn.v2.Arbeidsgiver;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class InntektsmeldingHendelse extends TidslinjeHendelse {
    private Arbeidsgiver arbeidsgiver;

}
