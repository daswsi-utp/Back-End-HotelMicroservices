package com.MicroserviceReservation.controller;

import com.MicroserviceReservation.Entities.booking;
import com.MicroserviceReservation.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<booking> create(@RequestBody booking booking) {
        booking saved = bookingService.createBooking(booking);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<booking> update(@PathVariable Long id, @RequestBody booking booking) {
        booking updated = bookingService.updateBooking(id, booking);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<booking> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping
    public List<booking> getAll() {
        return bookingService.getAllBookings();
    }
}
