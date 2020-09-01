package no.nav.foreldrepenger.historikk.config;

import static io.swagger.models.Scheme.HTTP;
import static io.swagger.models.Scheme.HTTPS;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.OAS_30;

import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(OAS_30)
                .protocols(Set.of(HTTP.toValue(), HTTPS.toValue()))
                .select()
                .apis(basePackage("no.nav.foreldrepenger.historikk"))
                .build();
    }
}
