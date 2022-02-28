package no.nav.foreldrepenger.historikk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI swaggerOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("Fpinfo-historikk")
                .description("REST-API for historikk for foreldrepenger/engangsstønad/svangerskapspenger")
                .version("v0.0.1")
                .license(new License().name("MIT").url("http://nav.no")));
    }
}
