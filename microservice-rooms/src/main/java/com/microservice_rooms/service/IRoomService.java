package com.microservice_rooms.service;

import com.microservice_rooms.entities.Room;

import java.util.List;
import java.util.Optional;

public interface IRoomService {
    List<Room> findAll();
    Optional<Room> findById(Long id);
    Room save(Room room);
    Room update(Room room, Long id); // Cambiado a Room
    void deleteById(Long id);
}
