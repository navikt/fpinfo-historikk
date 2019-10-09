package no.nav.foreldrepenger.historikk.config;

import static com.google.common.collect.Sets.newHashSet;
import static io.swagger.models.Scheme.HTTP;
import static io.swagger.models.Scheme.HTTPS;
import static no.nav.foreldrepenger.historikk.util.EnvUtil.isLocal;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import java.util.Set;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration implements EnvironmentAware {
    private Environment env;

    @Bean
    public Docket api() {
        return new Docket(SWAGGER_2)
                .protocols(protocol())
                .select()
                .apis(basePackage("no.nav.foreldrepenger.historikk"))
                .paths(PathSelectors.regex(".*dev.*"))
                .build();
    }

    private Set<String> protocol() {
        return isLocal(env) ? newHashSet(HTTP.toValue()) : newHashSet(HTTPS.toValue());
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }
}
