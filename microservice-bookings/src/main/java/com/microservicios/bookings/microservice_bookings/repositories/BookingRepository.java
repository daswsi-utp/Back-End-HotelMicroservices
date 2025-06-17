package com.microservicios.bookings.microservice_bookings.repositories;

import com.microservicios.bookings.microservice_bookings.entites.Booking;
import feign.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends CrudRepository<Booking,Long>
{
    long countByUserId(Long userId);

    @Query("SELECT DISTINCT b.userId FROM Booking b")
    List<Long> findDistinctUserIds();

    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Booking b WHERE b.userId = :userId")
    double sumTotalByUserId(@Param("userId") Long userId);

    @Query("SELECT MIN(b.checkIn) FROM Booking b WHERE b.userId = :userId")
    LocalDate findFirstBookingDateByUserId(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN SUM(CASE WHEN b.status = 'OPEN' THEN 1 ELSE 0 END) > 0 THEN 'ACTIVE' ELSE 'INACTIVE' END "
            + "FROM Booking b WHERE b.userId = :userId")
    String findOverallStatusByUserId(@Param("userId") Long userId);
}
