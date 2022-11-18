package no.nav.foreldrepenger.historikk.tjenester.tidslinje;


import lombok.Getter;
import lombok.experimental.SuperBuilder;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivDokument;

import java.util.List;

@Getter
@SuperBuilder
public class EttersendingHendelse extends TidslinjeHendelse {
    private List<ArkivDokument> dokumenter;
}
