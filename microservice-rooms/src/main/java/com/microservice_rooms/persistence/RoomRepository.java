package com.microservice_rooms.persistence;

import com.microservice_rooms.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
