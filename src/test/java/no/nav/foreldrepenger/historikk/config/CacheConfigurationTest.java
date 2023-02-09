package no.nav.foreldrepenger.historikk.config;

import no.nav.foreldrepenger.common.domain.Fødselsnummer;
import no.nav.foreldrepenger.common.util.TokenUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
    CacheConfiguration.class,
    CacheConfigurationTest.TestKlasse.class,
    ConcurrentMapCacheManager.class
})
@MockBean(classes = {TokenUtil.class})
class CacheConfigurationTest {

    @Autowired
    KeyGenerator keyGenerator;

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    TestKlasse testKlasse;

    @Autowired
    CacheManager cacheManager;

    @Test
    void cacheBrukerFødselsnummerSomKey() {
        // fødselsnummer kall nummer to returnerer cachet verdi
        var fødselsnummer = new Fødselsnummer("111");
        mockReturnerer(fødselsnummer);
        var resultat1 = testKlasse.cacheableMethod();
        var resultat2 = testKlasse.cacheableMethod();
        assertThat(resultat1).isEqualTo(0);
        assertThat(resultat2).isEqualTo(0);

        // annetFødselsnummer returnerer ikke verdi cachet på fødselsnummer
        var annetFødselsnummer = new Fødselsnummer("999");
        mockReturnerer(annetFødselsnummer);
        var resultat3 = testKlasse.cacheableMethod();
        assertThat(resultat3).isEqualTo(1);

        // nytt kall med fødselsnummer returnerer korrekt cachet verdi
        mockReturnerer(fødselsnummer);
        var resultat4 = testKlasse.cacheableMethod();
        assertThat(resultat4).isEqualTo(0);

        // annetFødselsnummer igjen for sikkerhets skyld
        mockReturnerer(annetFødselsnummer);
        var resultat5 = testKlasse.cacheableMethod();
        assertThat(resultat5).isEqualTo(1);
    }

    @Test
    void cachePropagererExceptionFraTokenUtil() {
        when(tokenUtil.autentisertBrukerOrElseThrowException()).thenThrow(IllegalStateException.class);
        assertThrows(IllegalStateException.class, () -> testKlasse.cacheableMethod());
    }

    private void mockReturnerer(Fødselsnummer fødselsnummer) {
        when(tokenUtil.autentisertBrukerOrElseThrowException()).thenReturn(fødselsnummer);
    }


    @EnableCaching
    static class TestKlasse {
        private static final AtomicInteger teller = new AtomicInteger();

        @Cacheable(value = "testcache",
                   keyGenerator = "autentisertFnrKeyGenerator")
        public int cacheableMethod() {
            return teller.getAndIncrement();
        }
    }

}
