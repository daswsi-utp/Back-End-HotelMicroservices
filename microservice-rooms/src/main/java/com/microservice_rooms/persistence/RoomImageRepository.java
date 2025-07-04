package com.microservice_rooms.persistence;

import com.microservice_rooms.entities.RoomImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {

}
