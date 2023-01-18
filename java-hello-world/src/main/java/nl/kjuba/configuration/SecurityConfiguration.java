package nl.kjuba.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import nl.kjuba.service.JpaUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JpaUserDetailsService jpaUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .httpBasic(withDefaults())
                .userDetailsService(jpaUserDetailsService)
                .authorizeRequests()
                .antMatchers("/web/user")
                .access("hasRole('ROLE_USER')")
                .antMatchers("/web/admin")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest()
                .authenticated()
                .and()

                /*
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/web/")
                .addLogoutHandler((request, response, authentication) -> {
                    authentication.setAuthenticated(false);
                    SecurityContextHolder.clearContext();
                    SecurityContextHolder.getContext().setAuthentication(null);
                    SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        session.invalidate();
                    }
                    try {
                        request.getSession().invalidate();
                        request.logout();
                    } catch (ServletException e) {
                        throw new RuntimeException(e);
                    }
                })
                .deleteCookies().invalidateHttpSession(true)
                .and()
                 */
                /*
                .logout()
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/web/").deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .addLogoutHandler(new HeaderWriterLogoutHandler(
                        new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.ALL)))
                .and()
                 */
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
