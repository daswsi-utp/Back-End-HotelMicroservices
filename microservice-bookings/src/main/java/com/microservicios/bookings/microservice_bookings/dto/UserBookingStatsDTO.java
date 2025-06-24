package com.microservicios.bookings.microservice_bookings.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserBookingStatsDTO
{
    // Datos de usuario
    private Long id;
    private String userName;
    private String userLastName;
    private String userEmail;
    private String cellPhone;

    // Estadísticas de reservas
    private LocalDate firstBookingDate;
    private long totalBookings;
    private double totalAmount;
    private String status;

}
