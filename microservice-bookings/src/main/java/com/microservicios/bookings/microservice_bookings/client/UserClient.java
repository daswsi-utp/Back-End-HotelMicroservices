package com.microservicios.bookings.microservice_bookings.client;

import com.microservicios.bookings.microservice_bookings.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="microservice-users")
public interface UserClient
{
    @GetMapping("/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}