package no.nav.foreldrepenger.historikk.tjenester.tidslinje;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivDokument;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, include = PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Søknadshendelse.class, name = "søknad"),
    @JsonSubTypes.Type(value = InntektsmeldingHendelse.class, name = "inntektsmelding"),
    @JsonSubTypes.Type(value = VedtakHendelse.class, name = "vedtak"),
    @JsonSubTypes.Type(value = EttersendingHendelse.class, name = "ettersending")
})
@SuperBuilder
@Getter
@ToString
@EqualsAndHashCode
public abstract class TidslinjeHendelse implements Comparable<TidslinjeHendelse> {
    private LocalDateTime opprettet;
    private AktørType aktørType;
    private TidslinjeHendelseType tidslinjeHendelseType;
    private List<ArkivDokument> dokumenter;

    public List<ArkivDokument> getDokumenter() {
        return dokumenter == null ? List.of() : dokumenter;
    }

    @Override
    public int compareTo(TidslinjeHendelse o) {
        return opprettet.compareTo(o.opprettet);
    }

}
