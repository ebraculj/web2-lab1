package com.example.web1.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {
    @GetMapping("/api/user")
    public Map<String, Object> getUser(@AuthenticationPrincipal OidcUser oidcUser){
        if (oidcUser != null){
            return Map.of("name", oidcUser.getFullName());
        }
        return Map.of("error", "Korisnik nije prijavljen.");
    }

}
