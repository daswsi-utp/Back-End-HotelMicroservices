package com.microservice.oauth.microservice_oauth.services;

import com.microservice.oauth.microservice_oauth.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements ReactiveUserDetailsService {

    @Autowired
    private WebClient.Builder client;

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return client.build()
                .get()
                .uri("/email/{email}", email)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(User.class)
                .map(this::convertToUserDetails)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new UsernameNotFoundException("Usuario no encontrado con email: " + email))
                ));
    }

    private UserDetails convertToUserDetails(User user) {
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities
        );
    }

}
