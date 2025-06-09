package com.MicroserviceReservation.service;

import com.MicroserviceReservation.Entities.booking;

import java.util.List;

public interface BookingService {
    booking createBooking(booking booking);
    booking updateBooking(Long id, booking booking);
    void deleteBooking(Long id);
    booking getBookingById(Long id);
    List<booking> getAllBookings();
}
