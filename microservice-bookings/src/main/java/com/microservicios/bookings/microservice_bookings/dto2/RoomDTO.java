package com.microservicios.bookings.microservice_bookings.dto2;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter

public class RoomDTO {
    private Long id;
    private Integer roomNumber;
    private Double pricePerNight;
    private Integer capacity;
}
