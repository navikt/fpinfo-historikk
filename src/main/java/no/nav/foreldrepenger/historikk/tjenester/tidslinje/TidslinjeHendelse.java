package no.nav.foreldrepenger.historikk.tjenester.tidslinje;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
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
public abstract class TidslinjeHendelse {
    private LocalDateTime opprettet;
    private AktørType aktørType;
    private TidslinjeHendelseType tidslinjeHendelseType;
}

enum AktørType { BRUKER, NAV, ARBEIDSGIVER }

enum TidslinjeHendelseType {
    FØRSTEGANGSSØKNAD,
    ENDRINGSSØKNAD,
    INNTEKTSMELDING,
    VEDTAK_FØRSTEGANG,
    VEDTAK_ENDRING,
    VEDTAK_TILBAKEKREVING,
    VENTER_INNTEKTSMELDING,
    ETTERSENDING,}

record TidslinjeHendelse2(AktørType aktørType,
                          TidslinjeHendelseType tidslinjeHendelseType,
                          List<ArkivDokument> vedlegg, // ny versjon som kombinerer joark  + manglende vedlegg
                          // + innsendte vedlegg
                          LocalDateTime tidspunkt

                          // typer:
                          // - inntektsmelding med arbeidsgiver
                          // - søknadstype med vedlegg
                          // - vedtak med vedlegg
                          // - ettersending
                          // - venter på..
) {


}
