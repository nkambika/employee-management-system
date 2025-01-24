package com.myproject.configurations;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired UserDetailsService userDetailsService;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/oauth2/**","/login", "/register", "/public/**","/css/**","/js/**","/icons/**").permitAll()
                .requestMatchers("/registration-form","/delete-employee/**").hasRole("ADMIN")
                .requestMatchers("/update-employee/**").hasAnyRole("ADMIN","GUEST","USER")
                .anyRequest().authenticated()
        );
        /*httpSecurity.oauth2Login(Customizer.withDefaults());*/
        // Configure OAuth2 Login
        httpSecurity.oauth2Login(oauth2 -> oauth2
                .loginPage("/")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/?error=true")
                .userInfoEndpoint(userInfo -> userInfo
                        .userAuthoritiesMapper(authorities -> {
                            // Map authorities, if needed
                            return authorities;
                        })
                )
        );
        httpSecurity.formLogin(form -> form
                .loginPage("/")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/?error=true")
                .permitAll()
        );
        httpSecurity.logout(logout->logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/dashboard")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .addLogoutHandler((request, response, authentication) -> {
                    if (authentication != null) {
                        SecurityContextHolder.clearContext();
                    }
                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        session.invalidate();
                    }
                })
        );
        httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        httpSecurity.exceptionHandling(ex->ex.accessDeniedPage("/access-denied"));httpSecurity.headers(headers -> headers
                .cacheControl(HeadersConfigurer.CacheControlConfig::disable)
        );
        return httpSecurity.build();
    }
}
