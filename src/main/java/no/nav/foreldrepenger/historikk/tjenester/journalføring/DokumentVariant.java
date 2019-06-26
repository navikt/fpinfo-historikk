package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import java.util.Arrays;

public class DokumentVariant {
    private final FilType filtype;
    private final VariantFormat variantformat;
    private final byte[] fysiskDokument;

    public DokumentVariant(FilType filtype, VariantFormat variantformat, byte[] fysiskDokument) {
        this.filtype = filtype;
        this.variantformat = variantformat;
        this.fysiskDokument = fysiskDokument;
    }

    public FilType getFiltype() {
        return filtype;
    }

    public VariantFormat getVariantformat() {
        return variantformat;
    }

    public byte[] getFysiskDokument() {
        return fysiskDokument;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[filtype=" + filtype + ", variantformat=" + variantformat
                + ", fysiskDokument=" + Arrays.toString(fysiskDokument) + "]";
    }
}
