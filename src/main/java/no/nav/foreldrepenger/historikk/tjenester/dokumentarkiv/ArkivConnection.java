package no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import no.nav.boot.conditionals.ConditionalOnNotProd;
import no.nav.foreldrepenger.historikk.config.JacksonConfiguration;
import no.nav.foreldrepenger.historikk.http.AbstractRestConnection;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.net.URI;
import java.util.Map;

@ConditionalOnNotProd
@Component
public class ArkivConnection extends AbstractRestConnection {

    private final ArkivOppslagConfig cfg;


    protected ArkivConnection(RestOperations restOperations, ArkivOppslagConfig config) {
        super(restOperations, config);
        this.cfg = config;
    }

    @Override
    public URI pingEndpoint() {
        return null;
    }

    public byte[] hentDok(String journalpostId, String dokumentInfoId) {
        var uri = cfg.hentDokumentUri(journalpostId, dokumentInfoId);
        return getForObject(uri, byte[].class);
    }

    public ArkivOppslagJournalposter journalposter(String ident) {
        if (ident == null) {
            throw new IllegalArgumentException("Mangler ident");
        }
        var requestBody = new Query(query(), Map.of("ident", ident));
        var wrappedResponse = postForEntity(cfg.dokumenter(), requestBody, DataWrapped.class);
        return wrappedResponse.data().journalposter();
    }

    private static String query() {
        return """
            query dokumentoversiktSelvbetjening($ident: String!) {
              dokumentoversiktSelvbetjening(ident: $ident, tema: [FOR]) {
                journalposter {
                  journalpostId
                  journalposttype
                  journalstatus
                  tittel
                  eksternReferanseId
                  relevanteDatoer {
                    dato
                    datotype
                  }
                  sak {
                    fagsakId
                    fagsaksystem
                    sakstype
                  }
                  dokumenter {
                    dokumentInfoId
                    brevkode
                    tittel
                    dokumentvarianter {
                      variantformat
                      filtype
                      brukerHarTilgang
                    }
                  }
                }
              }
            }""".stripIndent();
    }
    private record Query(String query, Map<String, String> variables) { }
    private record DataWrapped(Wrapped data) { };
    private record Wrapped(@JsonAlias("dokumentOversiktselvbetjening") ArkivOppslagJournalposter journalposter) { }

    public static void main(String[] args) throws JsonProcessingException {
        var response = """
            {
              "data": {
                "dokumentoversiktSelvbetjening": {
                  "journalposter": [
                    {
                      "journalpostId": "573783797",
                      "journalposttype": "I",
                      "journalstatus": "MOTTATT",
                      "tittel": "Søknad om foreldrepenger ved fødsel",
                      "eksternReferanseId": "a998dd58-520b-4288-805f-b85099929c55",
                      "relevanteDatoer": [
                        {
                          "dato": "2022-10-03T15:35:42",
                          "datotype": "DATO_OPPRETTET"
                        },
                        {
                          "dato": "2022-10-03T15:35:42",
                          "datotype": "DATO_DOKUMENT"
                        },
                        {
                          "dato": "2022-10-03T02:00",
                          "datotype": "DATO_REGISTRERT"
                        }
                      ],
                      "sak": null,
                      "dokumenter": [
                        {
                          "dokumentInfoId": "599046165",
                          "brevkode": "NAV 14-05.09",
                          "tittel": "Søknad om foreldrepenger ved fødsel",
                          "dokumentvarianter": [
                            {
                              "variantformat": "ARKIV",
                              "filtype": "PDF",
                              "brukerHarTilgang": true
                            }
                          ]
                        }
                      ]
                    },
                    {
                      "journalpostId": "573779335",
                      "journalposttype": "I",
                      "journalstatus": "MOTTATT",
                      "tittel": "Søknad om foreldrepenger ved adopsjon",
                      "eksternReferanseId": "a87ce02f-f1d4-4081-b167-29eec87b1269",
                      "relevanteDatoer": [
                        {
                          "dato": "2022-09-12T16:19:24",
                          "datotype": "DATO_OPPRETTET"
                        },
                        {
                          "dato": "2022-09-12T16:19:24",
                          "datotype": "DATO_DOKUMENT"
                        },
                        {
                          "dato": "2022-09-12T02:00",
                          "datotype": "DATO_REGISTRERT"
                        }
                      ],
                      "sak": null,
                      "dokumenter": [
                        {
                          "dokumentInfoId": "599040969",
                          "brevkode": "NAV 14-05.06",
                          "tittel": "Søknad om foreldrepenger ved adopsjon",
                          "dokumentvarianter": [
                            {
                              "variantformat": "ARKIV",
                              "filtype": "PDF",
                              "brukerHarTilgang": true
                            }
                          ]
                        },
                        {
                          "dokumentInfoId": "599040967",
                          "brevkode": null,
                          "tittel": "Dokumentasjon av dato for overtakelse av omsorg",
                          "dokumentvarianter": [
                            {
                              "variantformat": "ARKIV",
                              "filtype": "PDF",
                              "brukerHarTilgang": true
                            }
                          ]
                        },
                        {
                          "dokumentInfoId": "599040968",
                          "brevkode": null,
                          "tittel": "Dokumentasjon av dato for overtakelse av omsorg",
                          "dokumentvarianter": [
                            {
                              "variantformat": "ARKIV",
                              "filtype": "PDF",
                              "brukerHarTilgang": true
                            }
                          ]
                        },
                        {
                          "dokumentInfoId": "599040970",
                          "brevkode": null,
                          "tittel": "Dokumentasjon av aleneomsorg",
                          "dokumentvarianter": [
                            {
                              "variantformat": "ARKIV",
                              "filtype": "PDF",
                              "brukerHarTilgang": true
                            }
                          ]
                        },
                        {
                          "dokumentInfoId": "599040971",
                          "brevkode": null,
                          "tittel": "Dokumentasjon av dato for overtakelse av omsorg",
                          "dokumentvarianter": [
                            {
                              "variantformat": "ARKIV",
                              "filtype": "PDF",
                              "brukerHarTilgang": true
                            }
                          ]
                        }
                      ]
                    },
                    {
                      "journalpostId": "573779312",
                      "journalposttype": "I",
                      "journalstatus": "MOTTATT",
                      "tittel": "Søknad om foreldrepenger ved adopsjon",
                      "eksternReferanseId": "0ed5657c-4df5-4d11-b1d3-c9ba218ce0e8",
                      "relevanteDatoer": [
                        {
                          "dato": "2022-09-12T15:34:01",
                          "datotype": "DATO_OPPRETTET"
                        },
                        {
                          "dato": "2022-09-12T15:34:01",
                          "datotype": "DATO_DOKUMENT"
                        },
                        {
                          "dato": "2022-09-12T02:00",
                          "datotype": "DATO_REGISTRERT"
                        }
                      ],
                      "sak": null,
                      "dokumenter": [
                        {
                          "dokumentInfoId": "599040940",
                          "brevkode": "NAV 14-05.06",
                          "tittel": "Søknad om foreldrepenger ved adopsjon",
                          "dokumentvarianter": [
                            {
                              "variantformat": "ARKIV",
                              "filtype": "PDF",
                              "brukerHarTilgang": true
                            }
                          ]
                        },
                        {
                          "dokumentInfoId": "599040938",
                          "brevkode": null,
                          "tittel": "Dokumentasjon av dato for overtakelse av omsorg",
                          "dokumentvarianter": [
                            {
                              "variantformat": "ARKIV",
                              "filtype": "PDF",
                              "brukerHarTilgang": true
                            }
                          ]
                        },
                        {
                          "dokumentInfoId": "599040939",
                          "brevkode": null,
                          "tittel": "Dokumentasjon av dato for overtakelse av omsorg",
                          "dokumentvarianter": [
                            {
                              "variantformat": "ARKIV",
                              "filtype": "PDF",
                              "brukerHarTilgang": true
                            }
                          ]
                        }
                      ]
                    },
                    {
                      "journalpostId": "573779190",
                      "journalposttype": "I",
                      "journalstatus": "MOTTATT",
                      "tittel": "Søknad om foreldrepenger ved fødsel",
                      "eksternReferanseId": "d4b47c1e-dcf7-4f8f-806a-42d85e5fb1d7",
                      "relevanteDatoer": [
                        {
                          "dato": "2022-09-12T13:15:21",
                          "datotype": "DATO_OPPRETTET"
                        },
                        {
                          "dato": "2022-09-12T13:15:21",
                          "datotype": "DATO_DOKUMENT"
                        },
                        {
                          "dato": "2022-09-12T02:00",
                          "datotype": "DATO_REGISTRERT"
                        }
                      ],
                      "sak": null,
                      "dokumenter": [
                        {
                          "dokumentInfoId": "599040811",
                          "brevkode": "NAV 14-05.09",
                          "tittel": "Søknad om foreldrepenger ved fødsel",
                          "dokumentvarianter": [
                            {
                              "variantformat": "ARKIV",
                              "filtype": "PDF",
                              "brukerHarTilgang": true
                            }
                          ]
                        }
                      ]
                    },
                    {
                      "journalpostId": "573775415",
                      "journalposttype": "I",
                      "journalstatus": "MOTTATT",
                      "tittel": "Søknad om foreldrepenger ved fødsel",
                      "eksternReferanseId": "9008883e-59c1-46ab-b339-9da54b42c425",
                      "relevanteDatoer": [
                        {
                          "dato": "2022-08-16T15:33:19",
                          "datotype": "DATO_OPPRETTET"
                        },
                        {
                          "dato": "2022-08-16T15:33:19",
                          "datotype": "DATO_DOKUMENT"
                        },
                        {
                          "dato": "2022-08-16T02:00",
                          "datotype": "DATO_REGISTRERT"
                        }
                      ],
                      "sak": null,
                      "dokumenter": [
                        {
                          "dokumentInfoId": "599036520",
                          "brevkode": "NAV 14-05.09",
                          "tittel": "Søknad om foreldrepenger ved fødsel",
                          "dokumentvarianter": [
                            {
                              "variantformat": "ARKIV",
                              "filtype": "PDF",
                              "brukerHarTilgang": true
                            }
                          ]
                        }
                      ]
                    },
                    {
                      "journalpostId": "573774069",
                      "journalposttype": "I",
                      "journalstatus": "MOTTATT",
                      "tittel": "Syntetisk Inntektsmelding",
                      "eksternReferanseId": "AR0012267",
                      "relevanteDatoer": [
                        {
                          "dato": "2022-08-03T15:23:48",
                          "datotype": "DATO_OPPRETTET"
                        },
                        {
                          "dato": "2022-08-03T15:23:48",
                          "datotype": "DATO_DOKUMENT"
                        },
                        {
                          "dato": "2022-08-03T15:22:40",
                          "datotype": "DATO_REGISTRERT"
                        }
                      ],
                      "sak": null,
                      "dokumenter": [
                        {
                          "dokumentInfoId": "599034899",
                          "brevkode": "4936",
                          "tittel": "Syntetisk Inntektsmelding",
                          "dokumentvarianter": [
                            {
                              "variantformat": "ARKIV",
                              "filtype": "PDF",
                              "brukerHarTilgang": false
                            }
                          ]
                        }
                      ]
                    },
                    {
                      "journalpostId": "573774045",
                      "journalposttype": "I",
                      "journalstatus": "MOTTATT",
                      "tittel": "Søknad om foreldrepenger ved fødsel",
                      "eksternReferanseId": "ad1161a3-cb9f-467c-9de3-687887f6bce3",
                      "relevanteDatoer": [
                        {
                          "dato": "2022-08-03T12:49:29",
                          "datotype": "DATO_OPPRETTET"
                        },
                        {
                          "dato": "2022-08-03T12:49:29",
                          "datotype": "DATO_DOKUMENT"
                        },
                        {
                          "dato": "2022-08-03T02:00",
                          "datotype": "DATO_REGISTRERT"
                        }
                      ],
                      "sak": null,
                      "dokumenter": [
                        {
                          "dokumentInfoId": "599034874",
                          "brevkode": "NAV 14-05.09",
                          "tittel": "Søknad om foreldrepenger ved fødsel",
                          "dokumentvarianter": [
                            {
                              "variantformat": "ARKIV",
                              "filtype": "PDF",
                              "brukerHarTilgang": true
                            }
                          ]
                        }
                      ]
                    },
                    {
                      "journalpostId": "573773945",
                      "journalposttype": "I",
                      "journalstatus": "MOTTATT",
                      "tittel": "Søknad om foreldrepenger ved fødsel",
                      "eksternReferanseId": "c0ad5fae-a969-4505-b057-c2744daaf2f7",
                      "relevanteDatoer": [
                        {
                          "dato": "2022-08-02T13:42:53",
                          "datotype": "DATO_OPPRETTET"
                        },
                        {
                          "dato": "2022-08-02T13:42:53",
                          "datotype": "DATO_DOKUMENT"
                        },
                        {
                          "dato": "2022-08-02T02:00",
                          "datotype": "DATO_REGISTRERT"
                        }
                      ],
                      "sak": null,
                      "dokumenter": [
                        {
                          "dokumentInfoId": "599034762",
                          "brevkode": "NAV 14-05.09",
                          "tittel": "Søknad om foreldrepenger ved fødsel",
                          "dokumentvarianter": [
                            {
                              "variantformat": "ARKIV",
                              "filtype": "PDF",
                              "brukerHarTilgang": true
                            }
                          ]
                        }
                      ]
                    },
                    {
                      "journalpostId": "573773944",
                      "journalposttype": "U",
                      "journalstatus": "FERDIGSTILT",
                      "tittel": "Ikke mottatt søknad",
                      "eksternReferanseId": "ac600be3-d1c3-4596-af63-df221f3480aa",
                      "relevanteDatoer": [
                        {
                          "dato": "2022-08-02T13:40:49",
                          "datotype": "DATO_OPPRETTET"
                        },
                        {
                          "dato": "2022-08-02T13:40:49",
                          "datotype": "DATO_DOKUMENT"
                        },
                        {
                          "dato": "2022-08-02T13:40:49",
                          "datotype": "DATO_JOURNALFOERT"
                        }
                      ],
                      "sak": {
                        "fagsakId": "352007866",
                        "fagsaksystem": "FS36",
                        "sakstype": "FAGSAK"
                      },
                      "dokumenter": [
                        {
                          "dokumentInfoId": "599034761",
                          "brevkode": "IKKESO",
                          "tittel": "Ikke mottatt søknad",
                          "dokumentvarianter": [
                            {
                              "variantformat": "ARKIV",
                              "filtype": "PDF",
                              "brukerHarTilgang": true
                            }
                          ]
                        }
                      ]
                    },
                    {
                      "journalpostId": "573773943",
                      "journalposttype": "I",
                      "journalstatus": "JOURNALFOERT",
                      "tittel": "Syntetisk Inntektsmelding",
                      "eksternReferanseId": "AR0012266",
                      "relevanteDatoer": [
                        {
                          "dato": "2022-08-02T13:40:37",
                          "datotype": "DATO_OPPRETTET"
                        },
                        {
                          "dato": "2022-08-02T13:40:37",
                          "datotype": "DATO_DOKUMENT"
                        },
                        {
                          "dato": "2022-08-02T13:40:43",
                          "datotype": "DATO_JOURNALFOERT"
                        },
                        {
                          "dato": "2022-04-21T12:53:15",
                          "datotype": "DATO_REGISTRERT"
                        }
                      ],
                      "sak": {
                        "fagsakId": "352007866",
                        "fagsaksystem": "FS36",
                        "sakstype": "FAGSAK"
                      },
                      "dokumenter": [
                        {
                          "dokumentInfoId": "599034760",
                          "brevkode": "4936",
                          "tittel": "Syntetisk Inntektsmelding",
                          "dokumentvarianter": [
                            {
                              "variantformat": "ARKIV",
                              "filtype": "PDF",
                              "brukerHarTilgang": false
                            }
                          ]
                        }
                      ]
                    }
                  ]
                }
              }
            }
            """;

        var mapper = new JacksonConfiguration().customObjectmapper();

        mapper.readValue(response, DataWrapped.class);
    }

}
