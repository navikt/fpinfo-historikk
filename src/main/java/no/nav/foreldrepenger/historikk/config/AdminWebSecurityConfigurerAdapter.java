package no.nav.foreldrepenger.historikk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import static no.nav.foreldrepenger.historikk.tjenester.dittnav.DittNavAdminController.ADMIN;

@Configuration
@EnableWebSecurity
public class AdminWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private static final String OPPGAVEADMIN = "OPPGAVEADMIN";
    @Value("${admin.username}")
    private String username;
    @Value("${admin.password}")
    private String password;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        var encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        var userDetails = User.withUsername(username)
            .password(encoder.encode(password))
            .roles(OPPGAVEADMIN).build();
        auth.inMemoryAuthentication().withUser(userDetails);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        var adminPattern = "/**" + ADMIN + "/**";

        http.authorizeRequests()
            .antMatchers(adminPattern).hasRole(OPPGAVEADMIN)
            .and().csrf().disable()
            .httpBasic();
    }

}
