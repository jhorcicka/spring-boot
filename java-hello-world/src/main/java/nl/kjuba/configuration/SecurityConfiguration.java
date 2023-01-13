package nl.kjuba.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import nl.kjuba.service.JpaUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JpaUserDetailsService jpaUserDetailsService;

    public SecurityConfiguration(final JpaUserDetailsService jpaUserDetailsService) {
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                .antMatchers("/web/user")
                .access("hasRole('ROLE_USER')")
                .antMatchers("/web/admin")
                .access("hasRole('ROLE_ADMIN')")
                .and()
                .userDetailsService(jpaUserDetailsService)
                .httpBasic(withDefaults())
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
