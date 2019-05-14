package no.nav.foreldrepenger.historikk;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import no.nav.security.spring.oidc.api.EnableOIDCTokenValidation;

@EnableOIDCTokenValidation(ignore = { "org.springframework", "springfox.documentation" })
@SpringBootApplication
@SpringBootConfiguration
public class FPInfoHistorikkApplication {
    public static void main(String[] args) throws IOException {
        new SpringApplicationBuilder(FPInfoHistorikkApplication.class)
                // .properties("spring.cloud.vault.token=" + VaultUtils.getToken())
                .main(FPInfoHistorikkApplication.class)
                .run(args);
    }

    @Bean
    public HikariDataSource ds(@Value("${spring.datasource.url}") String url) {
        return new HikariDataSource(hikariConfig(url));
    }

    private HikariConfig hikariConfig(String url) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setConnectionTimeout(1000);
        config.setMinimumIdle(1);
        config.setMaximumPoolSize(3);
        config.setConnectionTestQuery("select 1");
        config.setDriverClassName("org.postgresql.Driver");
        Properties dsProperties = new Properties();
        config.setDataSourceProperties(dsProperties);
        return config;
    }
}