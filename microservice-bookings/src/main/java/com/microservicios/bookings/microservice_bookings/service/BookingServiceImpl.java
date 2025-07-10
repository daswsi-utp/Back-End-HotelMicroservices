package com.microservicios.bookings.microservice_bookings.service;

import com.microservicios.bookings.microservice_bookings.client.RoomClient;
import com.microservicios.bookings.microservice_bookings.client.UserClient;
import com.microservicios.bookings.microservice_bookings.dto.*;
import com.microservicios.bookings.microservice_bookings.dto2.RoomDTO;
import com.microservicios.bookings.microservice_bookings.dto2.RoomResponseDTO;
import com.microservicios.bookings.microservice_bookings.dto2.RoomsRequest;
import com.microservicios.bookings.microservice_bookings.entites.Booking;
import com.microservicios.bookings.microservice_bookings.repositories.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingServiceImpl implements IBookingService
{
    private final BookingRepository bookingRepository;
    private final UserClient userClient;
    private final RoomClient roomClient;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserClient userClient,
                              RoomClient roomClient) {
        this.bookingRepository = bookingRepository;
        this.userClient = userClient;
        this.roomClient = roomClient;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Booking> findAll() {
        return (List<Booking>) bookingRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public Booking save(Booking booking) {
        booking.setCheckIn(booking.getCheckIn());
        booking.setCheckOut(booking.getCheckOut());
        booking.setStatus(booking.getStatus());
        booking.setTotal(booking.getTotal());
        booking.setRoomId(booking.getRoomId());
        booking.setUserId(booking.getUserId());
        booking.setDiscount(booking.getDiscount());
        return bookingRepository.save(booking);
    }

    @Override
    public Optional<Booking> update(Booking booking, Long id) {
       Optional<Booking> bookingOptional = this.findById(id);
       return bookingOptional.map( bOp -> {
           bOp.setUserId(booking.getUserId());
           bOp.setRoomId(booking.getRoomId());
           bOp.setTotal(booking.getTotal());
           bOp.setStatus(booking.getStatus());
           bOp.setCheckOut(booking.getCheckOut());
           bOp.setCheckIn(booking.getCheckOut());
           bOp.setDiscount(booking.getDiscount());
           return Optional.of(bookingRepository.save(bOp));
       }).orElseGet(()->Optional.empty());
    }

    @Override
    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public BookingResponseDTO getBookingWithUser(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        UserDTO user = userClient.getUserById(booking.getUserId());

        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(booking.getId());
        dto.setRoomId(booking.getRoomId());
        dto.setCheckIn(booking.getCheckIn());
        dto.setCheckOut(booking.getCheckOut());
        dto.setTotal(booking.getTotal());
        dto.setStatus(booking.getStatus());
        dto.setDiscount(booking.getDiscount());

        dto.setUserId(user.getId());
        dto.setUserName(user.getName());
        dto.setUserLastName(user.getLastName());
        dto.setUserEmail(user.getEmail());
        dto.setCellPhone(user.getCellPhone());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public UserBookingStatsDTO getUserBookingStats(Long userId) {
        // 1) UserDTO via Feign
        UserDTO user = userClient.getUserById(userId);

        // 2) Estadísticas desde el repositorio
        long count      = bookingRepository.countByUserId(userId);
        double sum      = bookingRepository.sumTotalByUserId(userId);
        LocalDate first = bookingRepository.findFirstBookingDateByUserId(userId);
        String status   = bookingRepository.findOverallStatusByUserId(userId);

        // 3) Mapear al DTO nuevo
        UserBookingStatsDTO stats = new UserBookingStatsDTO();
        stats.setId(user.getId());
        stats.setUserName(user.getName());
        stats.setUserLastName(user.getLastName());
        stats.setUserEmail(user.getEmail());
        stats.setCellPhone(user.getCellPhone());

        stats.setFirstBookingDate(first);
        stats.setTotalBookings(count);
        stats.setTotalAmount(sum);
        stats.setStatus(status);

        return stats;
    }
    @Override
    @Transactional(readOnly = true)
    public List<UserBookingStatsDTO> getAllUsersBookingStats() {
        return bookingRepository.findDistinctUserIds().stream()
                .map(this::getUserBookingStats)
                .collect(Collectors.toList());
    }


    //a

    @Override
    @Transactional
    public RoomResponseDTO createBooking(RoomsRequest req) {
        UserDTO user = userClient.getUserById(req.getUserId());
        RoomDTO room = roomClient.getRoomById(req.getRoomId());

        long nights = ChronoUnit.DAYS.between(req.getCheckIn(), req.getCheckOut());
        if (nights <= 0) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la de inicio");
        }

        double total = nights * room.getPricePerNight();

        Booking booking = new Booking();
        booking.setUserId(req.getUserId());
        booking.setRoomId(req.getRoomId());
        booking.setCheckIn(req.getCheckIn());
        booking.setCheckOut(req.getCheckOut());
        booking.setTotal(total);
        booking.setStatus("PENDING");

        Booking saved = bookingRepository.save(booking);

        return new RoomResponseDTO(
                saved.getId(),
                user.getName() + " " + user.getLastName(),
                user.getEmail(),
                room.getRoomNumber(),
                saved.getCheckIn(),
                saved.getCheckOut(),
                saved.getTotal(),
                saved.getStatus()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public RoomResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking no encontrado: " + id));

        UserDTO user = userClient.getUserById(booking.getUserId());
        RoomDTO room = roomClient.getRoomById(booking.getRoomId());

        return new RoomResponseDTO(
                booking.getId(),
                user.getName() + " " + user.getLastName(),
                user.getEmail(),
                room.getRoomNumber(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getTotal(),
                booking.getStatus()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponseDTO> listAll() {
        return bookingRepository.findAll().stream()
                .map(b -> {
                    UserDTO user = userClient.getUserById(b.getUserId());
                    RoomDTO room = roomClient.getRoomById(b.getRoomId());
                    return new RoomResponseDTO(
                            b.getId(),
                            user.getName() + " " + user.getLastName(),
                            user.getEmail(),
                            room.getRoomNumber(),
                            b.getCheckIn(),
                            b.getCheckOut(),
                            b.getTotal(),
                            b.getStatus()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoomResponseDTO cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking no encontrado: " + id));

        booking.setStatus("CANCELLED");
        Booking updated = bookingRepository.save(booking);

        UserDTO user = userClient.getUserById(updated.getUserId());
        RoomDTO room = roomClient.getRoomById(updated.getRoomId());

        return new RoomResponseDTO(
                updated.getId(),
                user.getName() + " " + user.getLastName(),
                user.getEmail(),
                room.getRoomNumber(),
                updated.getCheckIn(),
                updated.getCheckOut(),
                booking.getTotal(),
                updated.getStatus()
        );
    }

/*
    @Override
    @Transactional
    public void updateBookingsStatusByUserId(Long userId, String status) {
        if (!status.equalsIgnoreCase("ACTIVE") && !status.equalsIgnoreCase("INACTIVE")) {
            throw new IllegalArgumentException("Only ACTIVE or INACTIVE status are allowed.");
        }

        List<Booking> bookings = bookingRepository.findByUserId(userId);
        for (Booking booking : bookings) {
            booking.setStatus(status.toUpperCase());
        }

        bookingRepository.saveAll(bookings);
    }*/
}




