package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagDokumentInfo;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagDokumentInfo.ArkivOppslagDokumentVariant;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagDokumentInfo.ArkivOppslagDokumentVariant.ArkivOppslagDokumentFiltype;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagDokumentInfo.ArkivOppslagDokumentVariant.ArkivOppslagDokumentVariantFormat;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagJournalStatus;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagJournalpostType;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagRelevantDato;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivOppslagJournalposter.ArkivOppslagJournalpost.ArkivOppslagRelevantDato.ArkivOppslagDatoType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ArkivMapper.class})
@EnableConfigurationProperties(ArkivOppslagConfig.class)
@TestPropertySource(properties = {
    "historikk.saf.baseUri=baseUri",
    "historikk.saf.api-base-uri=apiBaseUri"
})
class ArkivMapperTest {

    @Autowired
    private ArkivMapper mapper;
    private final static ArkivOppslagDokumentVariant defaultVariant = new ArkivOppslagDokumentVariant(
        ArkivOppslagDokumentVariantFormat.ARKIV,
        ArkivOppslagDokumentFiltype.PDF,
        true);
    private final List<ArkivOppslagRelevantDato> defaultDatoer = List.of(new ArkivOppslagRelevantDato(LocalDateTime.now(), DATO_OPPRETTET));


    @Test
    void markerHoveddokument() {
        // Første dokumentet tilknyttet journalpost anses som hoveddokument
        var dok1 = new ArkivOppslagDokumentInfo("1", Optional.empty(), Optional.empty(), List.of(defaultVariant));
        var dok2 = new ArkivOppslagDokumentInfo("2", Optional.empty(), Optional.empty(), List.of(defaultVariant));
        var jp = new ArkivOppslagJournalposter.ArkivOppslagJournalpost("42", ArkivOppslagJournalpostType.I,
            ArkivOppslagJournalStatus.JOURNALFOERT, Optional.empty(),
            Optional.empty(), defaultDatoer, Optional.empty(), List.of(dok1, dok2));
        var arkivDokumenter = mapper.map(new ArkivOppslagJournalposter(List.of(jp)));
        assertThat(arkivDokumenter).hasSize(2);
        assertTrue(arkivDokumenter.get(0).isHovedDokument());
        assertEquals("1", arkivDokumenter.get(0).getDokumentId());
        assertFalse(arkivDokumenter.get(1).isHovedDokument());
        assertEquals("2", arkivDokumenter.get(1).getDokumentId());
    }

}
