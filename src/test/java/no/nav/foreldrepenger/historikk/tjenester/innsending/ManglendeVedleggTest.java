package no.nav.foreldrepenger.historikk.tjenester.innsending;

import static java.util.stream.Collectors.toList;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType.ETTERSENDING_FORELDREPENGER;
import static no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType.INITIELL_FORELDREPENGER;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.DokumentType.I000001;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.DokumentType.I000002;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingType.LASTET_OPP;
import static no.nav.foreldrepenger.historikk.tjenester.innsending.InnsendingType.SEND_SENERE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;
import no.nav.foreldrepenger.historikk.tjenester.oppslag.Oppslag;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = Innsending.class)
public class ManglendeVedleggTest {

    private static final String REF = "666";
    private static final String SAKSNR = "42";
    @MockBean
    private JPAInnsendingRepository dao;
    @MockBean
    private Oppslag oppslag;

    @Autowired
    private Innsending innsending;

    @Test
    public void testIngenTidligereHendelser() {
        var info = innsending.vedleggsInfo(SAKSNR, REF);
        assertFalse(info.manglerVedlegg());
    }

    @Test
    public void testMangler1() {
        when(dao.findBySaksnrOrderByOpprettetAsc(eq(SAKSNR)))
                .thenReturn(List.of(innslag(I000001, INITIELL_FORELDREPENGER, SAKSNR, SEND_SENERE)));
        var info = innsending.vedleggsInfo(SAKSNR, REF);
        assertTrue(info.manglerVedlegg());
        assertEquals(1, info.getManglende().size());
        assertEquals(info.getManglende().get(0), I000001.name());
    }

    @Test
    public void testSisteInitielleOverskriverGamle() {
        when(dao.findBySaksnrOrderByOpprettetAsc(eq(SAKSNR)))
                .thenReturn(List.of(
                        innslag(I000001, INITIELL_FORELDREPENGER, SAKSNR, SEND_SENERE),
                        innslag(I000002, INITIELL_FORELDREPENGER, SAKSNR, SEND_SENERE)));
        var info = innsending.vedleggsInfo(SAKSNR, REF);
        assertTrue(info.manglerVedlegg());
        assertEquals(1, info.getManglende().size());
        assertEquals(info.getManglende().get(0), I000002.name());
    }

    @Test
    public void testInnsendingAvManglende() {
        when(dao.findBySaksnrOrderByOpprettetAsc(eq(SAKSNR)))
                .thenReturn(List.of(
                        innslag(I000001, INITIELL_FORELDREPENGER, SAKSNR, SEND_SENERE),
                        innslag(I000001, ETTERSENDING_FORELDREPENGER, SAKSNR, LASTET_OPP)));
        var info = innsending.vedleggsInfo(SAKSNR, REF);
        assertFalse(info.manglerVedlegg());
        assertTrue(info.getManglende().isEmpty());
    }

    @Test
    public void testInnsendingAvManglende1() {
        when(dao.findBySaksnrOrderByOpprettetAsc(eq(SAKSNR)))
                .thenReturn(List.of(
                        innslag(I000001, INITIELL_FORELDREPENGER, SAKSNR, SEND_SENERE, SEND_SENERE),
                        innslag(I000001, ETTERSENDING_FORELDREPENGER, SAKSNR, LASTET_OPP)));
        var info = innsending.vedleggsInfo(SAKSNR, REF);
        assertTrue(info.manglerVedlegg());
        assertEquals(1, info.getManglende().size());
        assertEquals(info.getManglende().get(0), I000001.name());
    }

    @Test
    public void testInnsendingAvManglende2() {
        when(dao.findBySaksnrOrderByOpprettetAsc(eq(SAKSNR)))
                .thenReturn(List.of(
                        innslag(I000001, INITIELL_FORELDREPENGER, SAKSNR, SEND_SENERE, SEND_SENERE),
                        innslag(I000001, ETTERSENDING_FORELDREPENGER, SAKSNR, LASTET_OPP),
                        innslag(I000002, INITIELL_FORELDREPENGER, SAKSNR, SEND_SENERE, SEND_SENERE, SEND_SENERE),
                        innslag(I000002, ETTERSENDING_FORELDREPENGER, SAKSNR, LASTET_OPP)));
        var info = innsending.vedleggsInfo(SAKSNR, REF);
        assertTrue(info.manglerVedlegg());
        assertEquals(2, info.getManglende().size());
        assertEquals(info.getManglende().get(0), I000002.name());
        assertEquals(info.getManglende().get(1), I000002.name());

    }

    private static JPAInnsendingInnslag innslag(DokumentType id, HendelseType hendelse, String saksnr,
            InnsendingType... typer) {
        var innslag = new JPAInnsendingInnslag();
        innslag.setId(id.ordinal());
        innslag.setHendelse(hendelse);
        innslag.setSaksnr(saksnr);
        innslag.setInnsendt(LocalDateTime.now());
        innslag.setVedlegg(vedlegg(id, typer));
        innslag.setReferanseId(UUID.randomUUID().toString());
        return innslag;
    }

    private static List<JPAInnsendingVedlegg> vedlegg(DokumentType id, InnsendingType... typer) {
        return Arrays.stream(typer)
                .map(t -> ettVedlegg(id, t))
                .collect(toList());
    }

    private static JPAInnsendingVedlegg ettVedlegg(DokumentType id, InnsendingType type) {
        var vedlegg = new JPAInnsendingVedlegg();
        vedlegg.setId(id.ordinal());
        vedlegg.setVedleggId(String.valueOf(id));
        vedlegg.setInnsendingType(type);
        return vedlegg;
    }

}
