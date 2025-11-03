package com.noxvodia.twitteer.controller;

import java.util.Base64;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
public class ApiController {

    @GetMapping("/auth/callback")
    public ResponseEntity<?> handleCallback(@RequestParam String code) {

        String clientId = "5miflgkc4h2edf4h3408u77c36";
        String clientSecret = "btrkp27pkv7mvsqjtj54nh47l2h3scbbi619e4tiitrfhrfsnee";
        String redirectUri = "http://localhost:3000/profileCreator"; // Tiene que ser EXACTAMENTE el mismo que está en Cognito

        String credentials = Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret).getBytes());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", clientId);
        formData.add("code", code);
        formData.add("redirect_uri", redirectUri);

        

        WebClient webClient = WebClient.builder()
                .baseUrl("https://us-east-1rgr4mlkxk.auth.us-east-1.amazoncognito.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        Map<String, Object> tokenResponse = webClient.post()
                .uri("/oauth2/token")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + credentials)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        return ResponseEntity.ok(tokenResponse);
    }
}
