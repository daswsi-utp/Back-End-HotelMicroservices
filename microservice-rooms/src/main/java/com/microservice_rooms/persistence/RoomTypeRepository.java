package com.microservice_rooms.persistence;

import com.microservice_rooms.entities.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

}
