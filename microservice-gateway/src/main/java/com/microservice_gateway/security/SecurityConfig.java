package com.microservice_gateway.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/.well-known/jwks.json",
                                "/actuator/health"
                        ).permitAll()
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/bookings/stats").permitAll()
                        .pathMatchers(HttpMethod.GET, "api/users/count").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/bookings/count").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/bookings/count").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/bookings/total-income").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/rooms/count").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/users/username/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/users/username/{useranme}").hasAnyRole("USER")
                        .pathMatchers(HttpMethod.GET,"/api/rooms/**").permitAll()
                        .pathMatchers(HttpMethod.PUT,"/api/rooms/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "api/bookings/detail").permitAll()
                        .pathMatchers( "/api/users").permitAll()
                        .pathMatchers( HttpMethod.PUT,"/api/rooms/{id}").permitAll()
                        .pathMatchers( "/api/rooms/images/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/bookings/save").permitAll()
                        .pathMatchers( "/api/tags").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/rooms").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/roomtype").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/oauth/login").permitAll()
                        .pathMatchers(HttpMethod.GET, "api/users/check-username").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtDecoder(reactiveJwtDecoder())
                                .jwtAuthenticationConverter(customJwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(5));

        WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        return NimbusReactiveJwtDecoder
                .withJwkSetUri("http://localhost:9100/.well-known/jwks.json")
                .webClient(webClient)
                .build();
    }

    private Converter<Jwt, Mono<? extends AbstractAuthenticationToken>> customJwtAuthenticationConverter() {
        return jwt -> {
            Collection<String> roles = jwt.getClaimAsStringList("roles");
            Collection<GrantedAuthority> authorities = roles == null ?
                    java.util.Collections.emptyList() :
                    roles.stream()
                            .map(role -> {
                                if (!role.startsWith("ROLE_")) {
                                    return new SimpleGrantedAuthority("ROLE_" + role);
                                } else {
                                    return new SimpleGrantedAuthority(role);
                                }
                            })
                            .collect(Collectors.toList());
            return Mono.just(new JwtAuthenticationToken(jwt, authorities));
        };
    }
}