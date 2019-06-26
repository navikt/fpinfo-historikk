package no.nav.foreldrepenger.historikk.tjenester.journalføring;

import java.util.Arrays;

public class DokumentVariant {
    private final FilType filType;
    private final VariantFormat variantFormat;
    private final byte[] fysiskDokument;

    public DokumentVariant(FilType filType, VariantFormat variantFormat, byte[] fysiskDokument) {
        this.filType = filType;
        this.variantFormat = variantFormat;
        this.fysiskDokument = fysiskDokument;
    }

    public FilType getFilType() {
        return filType;
    }

    public VariantFormat getVariantFormat() {
        return variantFormat;
    }

    public byte[] getFysiskDokument() {
        return fysiskDokument;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[filType=" + filType + ", variantFormat=" + variantFormat
                + ", fysiskDokument=" + Arrays.toString(fysiskDokument) + "]";
    }
}
