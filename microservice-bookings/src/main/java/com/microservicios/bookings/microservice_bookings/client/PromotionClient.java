package com.microservicios.bookings.microservice_bookings.client;

import com.microservicios.bookings.microservice_bookings.dto2.PromotionResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="microservice-promotions")
public interface PromotionClient {
    @GetMapping("/roomType/{roomTypeId}")
    List<PromotionResponseDTO> getPromotionByRoomTypeId(@PathVariable Long id);
}
