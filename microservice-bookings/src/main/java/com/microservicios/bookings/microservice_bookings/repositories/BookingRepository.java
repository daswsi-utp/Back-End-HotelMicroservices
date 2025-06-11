package com.microservicios.bookings.microservice_bookings.repositories;

import com.microservicios.bookings.microservice_bookings.entites.Booking;
import org.springframework.data.repository.CrudRepository;

public interface BookingRepository extends CrudRepository<Booking,Long>
{

}
