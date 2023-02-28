package no.nav.foreldrepenger.historikk.tjenester.tidslinje;

import no.nav.foreldrepenger.historikk.domain.AktørId;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.ArkivDokument;
import no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.Brevkode;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;
import no.nav.foreldrepenger.historikk.tjenester.felles.HistorikkInnslag;
import no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingInnslag;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.ArbeidsgiverInnslag;
import no.nav.foreldrepenger.historikk.tjenester.inntektsmelding.InntektsmeldingInnslag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Collections.emptyList;
import static no.nav.foreldrepenger.historikk.tjenester.dokumentarkiv.DokumentType.UTGÅENDE_DOKUMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TidslinjeMapperTest {

    private static final AtomicInteger teller = new AtomicInteger();

    @Test
    public void innslagSkalMappesTilHendelser() {
        var førstegangssøknadInnslag = innsendingInnslag(HendelseType.INITIELL_FORELDREPENGER);
        var endringssøknadInnslag = innsendingInnslag(HendelseType.ENDRING_FORELDREPENGER);
        var inntektsmeldingInnslag = im(HendelseType.INNTEKTSMELDING_NY);

        var innslag = List.of(endringssøknadInnslag, førstegangssøknadInnslag, inntektsmeldingInnslag);

        var tidslinje = TidslinjeMapper.map(innslag, List.of());

        assertThat(tidslinje.size()).isEqualTo(3);
        assertTrue(tidslinje.get(0) instanceof Søknadshendelse);
        assertTrue(tidslinje.get(1) instanceof Søknadshendelse);
        assertTrue(tidslinje.get(2) instanceof InntektsmeldingHendelse);

        assertThat(tidslinje.get(0).getAktørType()).isEqualTo(AktørType.BRUKER);
        assertThat(tidslinje.get(1).getTidslinjeHendelseType()).isEqualTo(TidslinjeHendelseType.ENDRINGSSØKNAD);
        var inntektsmeldingHendelse = (InntektsmeldingHendelse) tidslinje.get(2);
        assertEquals(inntektsmeldingHendelse.getArbeidsgiver().id(), inntektsmeldingInnslag.getArbeidsgiver().getId());
    }

    @Test
    public void hendelserSkalBerikesMedDokumenterFraArkiv() {
        var førstegangssøknadInnslag = innsendingInnslag(HendelseType.INITIELL_FORELDREPENGER);
        var endringssøknadInnslag = innsendingInnslag(HendelseType.ENDRING_FORELDREPENGER);

        List<HistorikkInnslag> innslag = List.of(endringssøknadInnslag, førstegangssøknadInnslag);
        var relevantDokument = ArkivDokument.builder()
            .journalpost(førstegangssøknadInnslag.getJournalpostId())
                               .build();
        var irrelevantDokument = ArkivDokument.builder()
                               .journalpost("-1")
                               .build();
        var tidslinje = TidslinjeMapper.map(innslag, List.of(relevantDokument, irrelevantDokument));

        assertThat(tidslinje.get(0).getDokumenter()).containsExactly(relevantDokument);
        assertThat(tidslinje.get(1).getDokumenter()).isEmpty();
    }


    @Test
    public void nyFørstegangssøknadGirHendelseNyFørstegangssøknad() {
        var innslagFørstegang0 = innsendingInnslag(HendelseType.INITIELL_FORELDREPENGER);
        var innslagFørstegang1 = innsendingInnslag(HendelseType.INITIELL_FORELDREPENGER);
        List<HistorikkInnslag> innslag = List.of(innslagFørstegang0, innslagFørstegang1);
        var tidslinje = TidslinjeMapper.map(innslag, List.of());
        assertThat(tidslinje.get(0).getTidslinjeHendelseType()).isEqualTo(TidslinjeHendelseType.FØRSTEGANGSSØKNAD);
        assertThat(tidslinje.get(1).getTidslinjeHendelseType()).isEqualTo(TidslinjeHendelseType.FØRSTEGANGSSØKNAD_NY);
    }

    @Test
    public void innvilgelsesbrevMappesTilVedtakshendelser() {
        var dokBuilder = ArkivDokument.builder();
        dokBuilder.type(UTGÅENDE_DOKUMENT);
        dokBuilder.mottatt(LocalDateTime.now());
        dokBuilder.saksnummer("42");
        dokBuilder.tittel("Vedtak");
        dokBuilder.journalpost("123");
        dokBuilder.brevkode("INVFOR");
        var tidslinje = TidslinjeMapper.map(emptyList(), List.of(dokBuilder.build()));
        assertThat(tidslinje.size()).isEqualTo(1);
        assertThat(tidslinje.get(0).getTidslinjeHendelseType()).isEqualTo(TidslinjeHendelseType.VEDTAK);
    }

    @Test
    public void utgåendeBrevSkalIkkeMappesTilVedtakshendelse() {
        var builder = ArkivDokument.builder();
        builder.type(UTGÅENDE_DOKUMENT);
        builder.brevkode("XYZ");
        builder.journalpost("123");
        var tidslinje = TidslinjeMapper.map(emptyList(), List.of(builder.build()));
        assertThat(tidslinje).size().isEqualTo(0);
    }

    @Test
    public void innhentingsBrevSkalMappesTilUtgåendebrevhendelse() {
        var builder = ArkivDokument.builder();
        builder.type(UTGÅENDE_DOKUMENT);
        builder.brevkode(Brevkode.INNOPP.name());
        builder.journalpost("123");
        var tidslinje = TidslinjeMapper.map(emptyList(), List.of(builder.build()));
        assertThat(tidslinje).size().isEqualTo(1);
        var hendelse = tidslinje.get(0);
        assertThat(hendelse.getTidslinjeHendelseType()).isEqualTo(TidslinjeHendelseType.UTGÅENDE_INNHENT_OPPLYSNINGER);
    }

    @Test
    public void etterlyseInntektsmeldingSkalMappesTilUtgåendebrevhendelse() {
        var dokument = ArkivDokument.builder()
            .journalpost("123")
            .brevkode(Brevkode.ELYSIM.name())
            .type(UTGÅENDE_DOKUMENT)
            .build();
        var tidslinje = TidslinjeMapper.map(emptyList(), List.of(dokument));
        assertThat(tidslinje).hasSize(1);
        assertThat(tidslinje.get(0).getTidslinjeHendelseType()).isEqualTo(TidslinjeHendelseType.UTGÅENDE_ETTERLYS_INNTEKTSMELDING);
    }

    private static InntektsmeldingInnslag im(HendelseType type) {
        var verdi = teller.getAndIncrement();
        var nyIm = new InntektsmeldingInnslag();
        nyIm.setHendelseType(type);
        nyIm.setArbeidsgiver(new ArbeidsgiverInnslag("4283478834", "Arbeidsgiver A/S"));
        nyIm.setJournalpostId(String.valueOf(verdi));
        nyIm.setInnsendt(LocalDateTime.now().plusDays(verdi));
        nyIm.setAktørId(AktørId.valueOf("42"));
        nyIm.setSaksnr("1234");
        nyIm.setOpprettet(LocalDateTime.now().plusDays(verdi));
        return nyIm;
    }

    private static InnsendingInnslag innsendingInnslag(HendelseType hendelseType) {
        var verdi = teller.getAndIncrement();
        var tidspunkt = LocalDateTime.now().plusDays(verdi);
        var innsendingInnslag = new InnsendingInnslag(hendelseType);
        innsendingInnslag.setBehandlingsdato(LocalDate.now());
        innsendingInnslag.setInnsendt(tidspunkt);
        innsendingInnslag.setOpprettet(tidspunkt);
        innsendingInnslag.setAktørId(AktørId.valueOf("42"));
        innsendingInnslag.setVedlegg(List.of());
        innsendingInnslag.setFnr(Fødselsnummer.valueOf("42"));
        innsendingInnslag.setSaksnr("1234");
        innsendingInnslag.setJournalpostId(String.valueOf(verdi));
        return innsendingInnslag;
    }

}
