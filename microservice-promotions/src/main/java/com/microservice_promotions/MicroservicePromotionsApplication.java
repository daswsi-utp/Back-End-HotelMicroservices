package com.microservice_promotions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.microservice_promotions.client")
@SpringBootApplication
public class MicroservicePromotionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicePromotionsApplication.class, args);
	}

}
