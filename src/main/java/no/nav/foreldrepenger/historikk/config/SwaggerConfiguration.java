package no.nav.foreldrepenger.historikk.config;

import static io.swagger.models.Scheme.HTTP;
import static io.swagger.models.Scheme.HTTPS;
import static java.util.stream.Collectors.toSet;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import java.util.Set;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.models.Scheme;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(SWAGGER_2)
                .protocols(allProtocols())
                .select()
                .apis(basePackage("no.nav.foreldrepenger.historikk"))
                .paths(PathSelectors.any())
                .build();
    }

    private static Set<String> allProtocols() {
        return Stream.of(HTTPS, HTTP)
                .map(Scheme::toValue)
                .collect(toSet());
    }
}
