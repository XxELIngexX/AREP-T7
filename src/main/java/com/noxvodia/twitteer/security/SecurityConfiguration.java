package com.noxvodia.twitteer.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

        @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    CognitoLogoutHandler cognitoLogoutHandler = new CognitoLogoutHandler();

    http
        .cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of(
                "http://localhost:3000", // local dev
                "https://xxelingexx.github.io/AREP-T7-Front" // GitHub Pages
            ));
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(true);
            return config;
        }))
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/api/**").authenticated()
            .anyRequest().permitAll()
        )
        .logout(logout -> logout
            .logoutSuccessHandler(cognitoLogoutHandler)
            .logoutSuccessUrl("https://xxelingexx.github.io/AREP-T7-Front") // si quieres, puedes agregar lógica condicional para Pages
        )
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt
                .jwkSetUri("https://cognito-idp.us-east-1.amazonaws.com/us-east-1_rgr4mLkxk/.well-known/jwks.json")
            )
        );

    return http.build();
}

}