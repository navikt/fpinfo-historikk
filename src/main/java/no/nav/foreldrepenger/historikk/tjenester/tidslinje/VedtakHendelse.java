package no.nav.foreldrepenger.historikk.tjenester.tidslinje;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;


@Jacksonized
@SuperBuilder
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode
public class VedtakHendelse extends TidslinjeHendelse {
    private VedtakType vedtakType;

    enum VedtakType { INNVILGELSE, REVURDERING }
}
