package com.microservice_rooms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceRoomsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceRoomsApplication.class, args);
	}

}
