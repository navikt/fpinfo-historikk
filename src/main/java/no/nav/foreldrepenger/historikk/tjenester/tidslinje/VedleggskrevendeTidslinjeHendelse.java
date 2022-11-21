package no.nav.foreldrepenger.historikk.tjenester.tidslinje;


import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@ToString(callSuper = true)
public class VedleggskrevendeTidslinjeHendelse extends TidslinjeHendelse {
    private final List<String> manglendeVedlegg;
}
