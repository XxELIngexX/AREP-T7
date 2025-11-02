// package com.noxvodia.twitteer.controller;

// import java.util.Map;
// import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ResponseBody;

// @Controller
// public class ApiController {
//     @GetMapping("/")
//     public String index() {
//         return "index"; // busca templates/index.html
//     }

//     @GetMapping("/home")
//     public String home() {
//         return "home"; // busca templates/home.html
//     }

//     @GetMapping("/profile")
//     public String profile() {
//         return "profile"; // busca templates/profile.html
//     }

//     @GetMapping("/userDetails")
//     @ResponseBody
//     public Map<String, Object> userDetails(OAuth2AuthenticationToken authentication) {
//         return authentication.getPrincipal().getAttributes();
//     }

//     @GetMapping("/profileCreator")
//     public String profileCreator(OAuth2AuthenticationToken authentication) {
//         return "profileCreator"; // busca templates/profileCreator.html
//     }

// }
