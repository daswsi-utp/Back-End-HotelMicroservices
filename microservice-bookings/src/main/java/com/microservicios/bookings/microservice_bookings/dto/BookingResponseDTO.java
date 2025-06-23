package com.microservicios.bookings.microservice_bookings.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookingResponseDTO
{

    private Long id;
    private Long roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Double total;
    private String status;

    // Datos del usuario
    private Long userId;
    private String userName;
    private String userLastName;
    private String userEmail;
    private String cellPhone;



}
