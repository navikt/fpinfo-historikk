package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.stream.Collectors;

public class VedleggsInfo {

    public static final VedleggsInfo NONE = new VedleggsInfo(emptyList(), emptyList());
    private final List<String> refs;
    private final List<String> manglende;

    public VedleggsInfo(List<String> refs, List<String> manglende) {
        this.refs = refs;
        this.manglende = manglende;
    }

    public List<String> getRefs() {
        return refs;
    }

    public List<String> getManglende() {
        return manglende;
    }

    public boolean manglerVedlegg() {
        return !manglende.isEmpty();
    }

    public String manglendeVedleggTekst() {
        return "Vi mangler følgende " + manglende.size() + " vedlegg: " + beskrivelseFor(manglende);

    }

    private static String beskrivelseFor(List<String> dokumentIds) {
        return dokumentIds.stream()
                .map(VedleggsInfo::beskrivelseFor)
                .collect(Collectors.joining(", "));
    }

    private static String beskrivelseFor(String dokumentId) {
        return DokumentType.valueOf(dokumentId).getBeskrivelse();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[refs=" + refs + ", manglende=" + manglende + "]";
    }

}
