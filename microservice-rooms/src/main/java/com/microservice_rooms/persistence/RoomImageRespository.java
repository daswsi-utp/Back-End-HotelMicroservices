package com.microservice_rooms.persistence;

import com.microservice_rooms.entities.RoomImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomImageRespository extends JpaRepository<RoomImage, Long> {

}
