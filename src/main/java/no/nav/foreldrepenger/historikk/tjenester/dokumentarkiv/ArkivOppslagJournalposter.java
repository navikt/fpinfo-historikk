package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import java.time.LocalDateTime;
import java.util.*;


public record ArkivOppslagJournalposter(List<ArkivOppslagJournalpost> journalposter) {

    public record ArkivOppslagJournalpost(String journalpostId,
                                   ArkivOppslagJournalpostType journalpostType,
                                   ArkivOppslagJournalStatus journalStatus,
                                   Optional<String> tittel,
                                   Optional<UUID> eksternReferanseId,
                                   List<ArkivOppslagRelevantDato> relevanteDatoer,
                                   Optional<ArkivOppslagSak> sak,
                                   List<ArkivOppslagDokumentInfo> dokumenter) {
        public enum ArkivOppslagJournalpostType {I, U, N}

        public enum ArkivOppslagJournalStatus {
            MOTTATT, JOURNALFOERT, EKSPEDERT, FERDIGSTILT, UNDER_ARBEID, FEILREGISTRERT, UTGAAR, AVBRUTT, UKJENT_BRUKER, RESERVERT, OPPLASTING_DOKUMENT, UKJENT
        }

        public record ArkivOppslagRelevantDato(LocalDateTime dato,
                                        ArkivOppslagDatoType datoType) {
            public enum ArkivOppslagDatoType {
                DATO_OPPRETTET, DATO_SENDT_PRINT, DATO_EKSPEDERT, DATO_JOURNALFOERT, DATO_REGISTRERT, DATO_AVS_RETUR, DATO_DOKUMENT
            }
        }

        public record ArkivOppslagSak(Optional<String> fagsakId,
                               Optional<String> fagsaksystem,
                               ArkivOppslagSakstype sakstype) {
            public enum ArkivOppslagSakstype {
                GENERELL_SAK, FAGSAK
            }
        }

        public record ArkivOppslagDokumentInfo(String dokumentInfoId,
                                        Optional<String> brevkode,
                                        Optional<String> tittel,
                                        List<ArkivOppslagDokumentVariant> dokumentVarianter) {

            public record ArkivOppslagDokumentVariant(ArkivOppslagDokumentVariantFormat variantFormat,
                                               ArkivOppslagDokumentFiltype filtype,
                                               Boolean brukerHarTilgang) {
                public enum ArkivOppslagDokumentVariantFormat {
                    ARKIV, SLADDET
                }

                public enum ArkivOppslagDokumentFiltype {
                    PDF, JPG, PNG
                }
            }
        }
    }
}

