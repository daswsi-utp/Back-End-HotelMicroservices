package com.microservice_promotions.client;

import com.microservice_promotions.dto.RoomDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "micro-rooms", url = "http://localhost:8090/api/rooms")
public interface RoomClient {
   @GetMapping("/{id}")
    RoomDTO getRoomById(@PathVariable Long id);
}
