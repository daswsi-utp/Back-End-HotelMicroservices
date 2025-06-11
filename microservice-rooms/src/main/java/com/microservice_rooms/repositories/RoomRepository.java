package com.microservice_rooms.repositories;

import com.microservice_rooms.entities.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room,Long> {
}
