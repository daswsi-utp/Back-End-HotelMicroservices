package com.microservicios.bookings.microservice_bookings.service;

import com.microservicios.bookings.microservice_bookings.dto.BookingResponseDTO;
import com.microservicios.bookings.microservice_bookings.dto.UserBookingStatsDTO;
import com.microservicios.bookings.microservice_bookings.entites.Booking;

import java.util.List;
import java.util.Optional;

public interface IBookingService
{
    List<Booking> findAll();
    Optional<Booking> findById(Long id);
    Booking save(Booking booking);
    Optional<Booking> update(Booking booking, Long id);
    void deleteById(Long id);
    BookingResponseDTO getBookingWithUser(Long bookingId);
    UserBookingStatsDTO getUserBookingStats(Long userId);
    List<UserBookingStatsDTO> getAllUsersBookingStats();
}
