package com.microservice_gateway.warmup_config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@AllArgsConstructor
public class WarmUpConfig {
    private final WebClient.Builder webClientBuilder;

    @Scheduled(fixedRate = 30000)
    public void warmUpOAuth(){
        webClientBuilder.build()
                .get()
                .uri("https://hotel-oauth-production.up.railway.app")
                .retrieve()
                .toBodilessEntity()
                .subscribe(
                        r -> System.out.println("Oauth warm-up OK"),
                        e -> System.out.println("Oauth warm-up failed: " + e.getMessage())
                );
    }

}
