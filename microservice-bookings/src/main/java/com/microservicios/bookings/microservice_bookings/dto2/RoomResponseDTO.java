package com.microservicios.bookings.microservice_bookings.dto2;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
@AllArgsConstructor
@Getter
@Setter
public class RoomResponseDTO {

    private Long id;
    private String guestName;
    private String guestEmail;
    private Integer roomNumber;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double total;
    private String status;
}