package com.microservicios.bookings.microservice_bookings.client;
import com.microservicios.bookings.microservice_bookings.dto2.RoomDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="microservice-rooms")
public interface RoomClient {
    @GetMapping("/rooms/{id}")
    RoomDTO getRoomById(@PathVariable("id") Long id);
}