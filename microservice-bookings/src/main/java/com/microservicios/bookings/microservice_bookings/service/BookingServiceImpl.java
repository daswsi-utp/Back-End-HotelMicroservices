package com.microservicios.bookings.microservice_bookings.service;

import com.microservicios.bookings.microservice_bookings.client.UserClient;
import com.microservicios.bookings.microservice_bookings.dto.BookingResponseDTO;
import com.microservicios.bookings.microservice_bookings.dto.UserBookingStatsDTO;
import com.microservicios.bookings.microservice_bookings.dto.UserDTO;
import com.microservicios.bookings.microservice_bookings.entites.Booking;
import com.microservicios.bookings.microservice_bookings.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements IBookingService
{
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserClient userClient;

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

}
