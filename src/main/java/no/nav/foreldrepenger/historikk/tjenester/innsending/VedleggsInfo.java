package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;

public record VedleggsInfo(List<String> innsendte, List<String> manglende) {

    public static final VedleggsInfo NONE = new VedleggsInfo(emptyList(), emptyList());

    public VedleggsInfo(List<String> innsendte, List<String> manglende) {
        this.innsendte = Optional.ofNullable(innsendte).orElse(emptyList());
        this.manglende = Optional.ofNullable(manglende).orElse(emptyList());
    }

    public boolean manglerVedlegg() {
        return !manglende.isEmpty();
    }
}
