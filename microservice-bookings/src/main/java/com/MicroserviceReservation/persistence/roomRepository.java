package com.MicroserviceReservation.persistence;

import com.MicroserviceReservation.Entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface roomRepository extends JpaRepository<Room, Long> {

}
