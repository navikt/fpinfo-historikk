package no.nav.foreldrepenger.historikk.config;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(SWAGGER_2)
                .protocols(protocols("http", "https"))
                .select()
                .apis(basePackage("no.nav.foreldrepenger.historikk"))
                .paths(PathSelectors.any())
                .build();
    }

    private static Set<String> protocols(String... schemes) {
        return stream(schemes)
                .collect(toSet());
    }
}
