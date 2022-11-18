package no.nav.foreldrepenger.historikk.tjenester.tidslinje;

import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
public class VedtakHendelse extends TidslinjeHendelse {
    private VedtakType vedtakType;

    enum VedtakType { INNVILGELSE, REVURDERING }
}
