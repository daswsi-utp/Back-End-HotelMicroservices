package com.MicroserviceReservation.persistence;

import com.MicroserviceReservation.Entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface bookingRepository extends JpaRepository<Booking,Long> {

}
