package no.nav.foreldrepenger.historikk.tjenester.dittnav;

import java.time.Instant;

import org.springframework.core.env.Environment;

import no.nav.brukernotifikasjon.schemas.Beskjed;
import no.nav.brukernotifikasjon.schemas.Done;
import no.nav.brukernotifikasjon.schemas.Oppgave;
import no.nav.foreldrepenger.historikk.domain.Fødselsnummer;
import no.nav.foreldrepenger.historikk.tjenester.felles.HendelseType;
import no.nav.foreldrepenger.historikk.tjenester.felles.YtelseType;
import no.nav.foreldrepenger.historikk.util.EnvUtil;

public final class DittNavMapper {

    private static final int SIKKERHETSNIVÅ = 3;

    private DittNavMapper() {

    }

    public static Oppgave oppgave(Fødselsnummer fnr, String grupperingsId, String tekst, String url) {
        return Oppgave.newBuilder()
                .setFodselsnummer(fnr.getFnr())
                .setGrupperingsId(grupperingsId)
                .setLink(url)
                .setSikkerhetsnivaa(SIKKERHETSNIVÅ)
                .setTekst(tekst)
                .setTidspunkt(Instant.now().toEpochMilli()).build();
    }

    public static Done done(Fødselsnummer fnr, String grupperingsId) {
        return Done.newBuilder()
                .setFodselsnummer(fnr.getFnr())
                .setGrupperingsId(grupperingsId)
                .setTidspunkt(Instant.now().toEpochMilli()).build();
    }

    public static Beskjed beskjed(Fødselsnummer fnr, String grupperingsId, String tekst, String url) {
        return Beskjed.newBuilder()
                .setFodselsnummer(fnr.getFnr())
                .setGrupperingsId(grupperingsId)
                .setLink(url)
                .setSikkerhetsnivaa(SIKKERHETSNIVÅ)
                .setTekst(tekst)
                .setTidspunkt(Instant.now().toEpochMilli()).build();
    }

    public static String url(YtelseType ytelseType, Environment env) {
        switch (ytelseType) {
        case ES:
            return es(env);
        case SVP:
            return svp(env);
        case FP:
        default:
            return fp(env);
        }
    }

    public static String url(HendelseType hendelse, Environment env) {
        if (hendelse.erEngangsstønad()) {
            return es(env);
        }
        if (hendelse.erForeldrepenger()) {
            return fp(env);
        }
        if (hendelse.erSvangerskapspenger()) {
            return svp(env);
        }
        return fp(env);
    }

    private static String fp(Environment env) {
        return EnvUtil.isDev(env) ? "https://foreldrepengesoknad-q.nav.no/" : "https://foreldrepengesoknad.nav.no";
    }

    private static String svp(Environment env) {
        return EnvUtil.isDev(env) ? "https://svangerskapspenger-q.nav.no/" : "https://svangerskapspenger.nav.no";
    }

    private static String es(Environment env) {
        return EnvUtil.isDev(env) ? "https://engangsstonad-q.nav.no/" : "https://engangsstonad.nav.no";
    }

}
