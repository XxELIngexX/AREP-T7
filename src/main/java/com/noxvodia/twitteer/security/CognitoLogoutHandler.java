package com.noxvodia.twitteer.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;


public class CognitoLogoutHandler extends SimpleUrlLogoutSuccessHandler {


    private final String domain = "https://econexia.auth.us-east-1.amazoncognito.com";

   
    private final String logoutRedirectUrl = "http://localhost:3000";

    
    private final String userPoolClientId = "5miflgkc4h2edf4h3408u77c36";

    /**
     * URL de logout de Cognito y redirige .
     * Formato:
     * https://{domain}/logout?client_id={userPoolClientId}&logout_uri={logoutRedirectUrl}
     */
    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        return UriComponentsBuilder
                .fromUri(URI.create(domain + "/logout"))
                .queryParam("client_id", userPoolClientId)
                .queryParam("logout_uri", logoutRedirectUrl)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUriString();
    }
}
