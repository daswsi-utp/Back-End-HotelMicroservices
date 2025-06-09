package com.MicroserviceReservation.persistence;

import com.MicroserviceReservation.Entities.booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface bookingRepository extends JpaRepository<booking,Long> {

}
