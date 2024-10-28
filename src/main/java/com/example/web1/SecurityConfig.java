package com.example.web1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@EnableWebSecurity
@CrossOrigin(origins = "*")
public class SecurityConfig {
    private final LogoutHandler logoutHandler;

    public SecurityConfig(LogoutHandler logoutHandler){
        this.logoutHandler = logoutHandler;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Onemogućavanje CORS-a i CSRF-a (samo za potrebe testiranja)
        http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable());
         http.authorizeHttpRequests(auth -> {
            try{
                auth.requestMatchers("/api/ticket/create").hasAuthority("SCOPE_write:ticket");
                auth.requestMatchers("/api/ticket/ticket-data/{uuid}").authenticated()
                        .and().oauth2Login().and().logout()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .addLogoutHandler(logoutHandler);
                //auth.requestMatchers("/api/ticket/create").authenticated();
                auth.requestMatchers("/api/ticket/count").permitAll();
                auth.anyRequest().permitAll();
            }catch (Exception e){
                e.printStackTrace();
            }
        })
        // Konfiguracija za JWT autentifikaciju
        .oauth2ResourceServer(auth -> auth.jwt());

        return http.build();
    }
}
