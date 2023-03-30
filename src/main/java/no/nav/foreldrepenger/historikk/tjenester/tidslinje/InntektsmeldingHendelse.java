package no.nav.foreldrepenger.historikk.tjenester.tidslinje;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import no.nav.foreldrepenger.common.innsyn.Arbeidsgiver;

@Getter
@SuperBuilder
@ToString(callSuper = true)
public class InntektsmeldingHendelse extends TidslinjeHendelse {
    private Arbeidsgiver arbeidsgiver;

}
