package com.microservicios.bookings.microservice_bookings.controller;

import com.microservicios.bookings.microservice_bookings.dto.BookingResponseDTO;
import com.microservicios.bookings.microservice_bookings.dto2.RoomResponseDTO;
import com.microservicios.bookings.microservice_bookings.dto.UserBookingStatsDTO;
import com.microservicios.bookings.microservice_bookings.dto2.RoomsRequest;
import com.microservicios.bookings.microservice_bookings.entites.Booking;
import com.microservicios.bookings.microservice_bookings.service.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/api/bookings")
public class BooKingController
{
    @Autowired
    private IBookingService bookingService;

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings (){
        return ResponseEntity.ok(bookingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingsById(@PathVariable Long id){
        return bookingService.findById(id)
                .map(us -> ResponseEntity.ok(us)).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBookings(@RequestBody Booking booking, @PathVariable Long id){
        return  bookingService.update(booking,id).
                map( userUpdate -> ResponseEntity.status(HttpStatus.CREATED).body(userUpdate))
                .orElseGet(()->ResponseEntity.noContent().build());

    }
    @PostMapping
    public ResponseEntity<Booking> createBookings(@RequestBody Booking booking){
        Booking savedBooking =bookingService.save(booking);
        BookingResponseDTO dto = bookingService.getBookingWithUser(savedBooking.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookings(@PathVariable Long id){
        bookingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/full/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingWithUser(@PathVariable Long id) {
        BookingResponseDTO dto = bookingService.getBookingWithUser(id);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/stats")
    public ResponseEntity<List<UserBookingStatsDTO>> getAllUsersStats() {
        List<UserBookingStatsDTO> stats = bookingService.getAllUsersBookingStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/{userId}")
    public ResponseEntity<UserBookingStatsDTO> getUserStats(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getUserBookingStats(userId));
    }


    //ff
    @PostMapping("/save")
    public ResponseEntity<RoomResponseDTO> createBookingDto(@RequestBody RoomsRequest req) {
        RoomResponseDTO dto = bookingService.createBooking(req);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dto);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<RoomResponseDTO> getBookingDtoById(@PathVariable Long id) {
        RoomResponseDTO dto = bookingService.getBookingById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/detail")
    public ResponseEntity<List<RoomResponseDTO>> listAllBookingDtos() {
        List<RoomResponseDTO> dtos = bookingService.listAll();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/detail/{id}/cancel")
    public ResponseEntity<RoomResponseDTO> cancelBookingDto(@PathVariable Long id) {
        RoomResponseDTO dto = bookingService.cancelBooking(id);
        return ResponseEntity.ok(dto);
    }



}
