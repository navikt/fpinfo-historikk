package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import java.time.LocalDateTime;
import java.util.*;


public record ArkivOppslagJournalposter(List<ArkivOppslagJournalpost> journalposter) {

    record ArkivOppslagJournalpost(String journalpostId,
                                   ArkivOppslagJournalpostType journalpostType,
                                   ArkivOppslagJournalStatus journalStatus,
                                   Optional<String> tittel,
                                   Optional<UUID> eksternReferanseId,
                                   List<ArkivOppslagRelevantDato> relevanteDatoer,
                                   Optional<ArkivOppslagSak> sak,
                                   List<ArkivOppslagDokumentInfo> dokumenter) {
        enum ArkivOppslagJournalpostType {I, U, N}

        enum ArkivOppslagJournalStatus {
            MOTTATT, JOURNALFOERT, EKSPEDERT, FERDIGSTILT, UNDER_ARBEID, FEILREGISTRERT, UTGAAR, AVBRUTT, UKJENT_BRUKER, RESERVERT, OPPLASTING_DOKUMENT, UKJENT
        }

        record ArkivOppslagRelevantDato(LocalDateTime dato,
                                        ArkivOppslagDatoType datoType) {
            enum ArkivOppslagDatoType {
                DATO_OPPRETTET, DATO_SENDT_PRINT, DATO_EKSPEDERT, DATO_JOURNALFOERT, DATO_REGISTRERT, DATO_AVS_RETUR, DATO_DOKUMENT
            }
        }

        record ArkivOppslagSak(Optional<String> fagsakId,
                               Optional<String> fagsaksystem,
                               ArkivOppslagSakstype sakstype) {
            enum ArkivOppslagSakstype {
                GENERELL_SAK, FAGSAK
            }
        }

        record ArkivOppslagDokumentInfo(String dokumentInfoId,
                                        Optional<String> brevkode,
                                        Optional<String> tittel,
                                        List<ArkivOppslagDokumentVariant> dokumentVarianter) {

            record ArkivOppslagDokumentVariant(ArkivOppslagDokumentVariantFormat variantFormat,
                                               ArkivOppslagDokumentFiltype filtype,
                                               Boolean brukerHarTilgang) {
                enum ArkivOppslagDokumentVariantFormat {
                    ARKIV, SLADDET
                }

                enum ArkivOppslagDokumentFiltype {
                    PDF, JPG, PNG
                }
            }
        }
    }
}

