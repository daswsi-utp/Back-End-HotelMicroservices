package com.microservice_gateway.warmup_config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@AllArgsConstructor
public class WarmUpConfig {
    private final WebClient.Builder webClienBuilder;

    @Value("${oauth.warmup-url}")
    private String oauthServiceUrl;

    @Scheduled(fixedRate = 30_000)
    public void warmUpAuth(){
        webClienBuilder.build()
                .get()
                .uri(oauthServiceUrl + "/actuator/health")
                .retrieve()
                .toBodilessEntity()
                .subscribe(
                        r -> System.out.println("[WARMUP] OAuth warm-up OK"),
                        e -> System.out.println("[WARMUP] Oauth warm-up failed: " + e.getMessage())
                );
    }

}
