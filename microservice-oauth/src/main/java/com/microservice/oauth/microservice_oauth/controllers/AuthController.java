package com.microservice.oauth.microservice_oauth.controllers;

import com.microservice.oauth.microservice_oauth.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    private final ReactiveAuthenticationManager authenticationManager;

    private final JwtEncoder jwtEncoder;

    public AuthController(ReactiveAuthenticationManager authenticationManager, JwtEncoder jwtEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody User user) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

        return authenticationManager.authenticate(authToken)
                .flatMap(auth -> {
                    String token = generateToken(auth);
                    Map<String, String> response = new HashMap<>();
                    response.put("access_token", token);
                    return Mono.just(ResponseEntity.ok(response));
                })
                .onErrorResume(e -> {
                    Map<String, String> error = new HashMap<>();
                    String msg = "Credenciales inválidas";

                    if (e instanceof UsernameNotFoundException) {
                        msg = "Email incorrecto";
                    } else if (e instanceof BadCredentialsException) {
                        msg = "Contraseña incorrecta";
                    }

                    error.put("error", msg);
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error));
                });
    }

    private String generateToken(Authentication authentication) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(4, ChronoUnit.HOURS))
                .claim("roles", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .build();

        JwsHeader jwsHeader = JwsHeader.with(() -> "RS256").build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}