package com.microservice.oauth.microservice_oauth.controllers;

import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import com.nimbusds.jose.jwk.JWKSet;

import java.util.Map;

@RestController
public class JwksController {

    private final JWKSource<SecurityContext> jwkSource;

    public JwksController(JWKSource<SecurityContext> jwkSource) {
        this.jwkSource = jwkSource;
    }

    @GetMapping("/.well-known/jwks.json")
    public Mono<Map<String, Object>> getJwks() {
        try {
            JWKSet jwkSet = ((ImmutableJWKSet<SecurityContext>) jwkSource).getJWKSet();
            return Mono.just(jwkSet.toJSONObject());
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

}