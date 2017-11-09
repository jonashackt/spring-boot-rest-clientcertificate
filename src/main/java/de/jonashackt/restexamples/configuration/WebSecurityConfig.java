package de.jonashackt.restexamples.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /*
     * Enable x509 client authentication.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.x509();
    }

    /*
     * Create an in-memory authentication manager. We create 1 user (sdg which
     * is the CN of the client certificate) which has a role of USER.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("sdg")
                .password("none")
                .roles("USER");
    }
}
