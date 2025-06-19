package com.microservice.microservice_message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching  
public class ReservaHotelApplication {

    public static void main(String[] args) {
        
        SpringApplication.run(ReservaHotelApplication.class, args);
    }
}
