package no.nav.foreldrepenger.historikk.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;


@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI swaggerOpenAPI(BuildProperties buildProperties) {
        return new OpenAPI()
            .info(new Info().title("fpinfo-historikk")
                .description("REST-API for historikk for foreldrepenger/engangsstønad/svangerskapspenger")
                .version(buildProperties.getVersion())
                .license(new License().name("MIT").url("http://nav.no")))
            .components(new Components()
                .addSecuritySchemes("bearer-key", new SecurityScheme()
                    .type(HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")))
            ;
    }
}
