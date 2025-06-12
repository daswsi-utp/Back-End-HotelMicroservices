package com.microservice.promotions.microservice_promotions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroservicePromotionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicePromotionsApplication.class, args);
	}

}
