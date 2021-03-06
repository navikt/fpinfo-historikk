package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.stream.Collectors;

public class VedleggsInfo {

    private static final int MAX_LENGDE = 500;

    public static final VedleggsInfo NONE = new VedleggsInfo(emptyList(), emptyList());
    private final List<String> innsendte;
    private final List<String> manglende;

    public VedleggsInfo(List<String> innsendte, List<String> manglende) {
        this.innsendte = innsendte;
        this.manglende = manglende;
    }

    public List<String> getInnsendte() {
        return innsendte;
    }

    public List<String> getManglende() {
        return manglende;
    }

    public boolean manglerVedlegg() {
        return !manglende.isEmpty();
    }

    public String manglendeVedleggTekst() {
        var intro = "Vi mangler " + manglende.size() + " vedlegg";
        var tekst = intro + ": " + beskrivelseFor(manglende);
        return tekst.length() >= MAX_LENGDE ? intro : tekst;

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
        return getClass().getSimpleName() + "[innsendte=" + innsendte + ", manglende=" + manglende + "]";
    }

}
