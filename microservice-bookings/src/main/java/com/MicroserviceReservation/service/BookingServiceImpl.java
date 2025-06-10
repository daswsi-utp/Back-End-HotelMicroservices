package com.MicroserviceReservation.service;

import com.MicroserviceReservation.Entities.Booking;
import com.MicroserviceReservation.persistence.bookingRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private bookingRepository bookingRepository;

    public BookingServiceImpl(bookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(Long id, Booking booking) {
        Booking current = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found: " + id));

        current.setRoom(booking.getRoom());
        current.setUser(booking.getUser());
        current.setCheckInDate(booking.getCheckInDate());
        current.setCheckOutDate(booking.getCheckOutDate());
        current.setTotal(booking.getTotal());
        current.setStatus(booking.getStatus());

        return bookingRepository.save(current);
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found: " + id));
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}
