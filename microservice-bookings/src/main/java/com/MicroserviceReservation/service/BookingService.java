package com.MicroserviceReservation.service;

import com.MicroserviceReservation.Entities.Booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking);
    Booking updateBooking(Long id, Booking booking);
    void deleteBooking(Long id);
    Booking getBookingById(Long id);
    List<Booking> getAllBookings();
}
