package com.microservicios.bookings.microservice_bookings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicroserviceBookingsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceBookingsApplication.class, args);
	}

}
