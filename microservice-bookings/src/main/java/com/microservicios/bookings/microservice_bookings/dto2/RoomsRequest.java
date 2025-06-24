package com.microservicios.bookings.microservice_bookings.dto2;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter
public class RoomsRequest {
    private Long userId;
    private Long roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String status;

    //aca calculo del precio
}
