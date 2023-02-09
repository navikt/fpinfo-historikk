package no.nav.foreldrepenger.historikk.config;

import no.nav.foreldrepenger.common.util.TokenUtil;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfiguration {

    @Bean
    public KeyGenerator autentisertFnrKeyGenerator(TokenUtil tokenUtil) {
        return (target, method, params) -> tokenUtil.autentisertBrukerOrElseThrowException().value();
    }

}
