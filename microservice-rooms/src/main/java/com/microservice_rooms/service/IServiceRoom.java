package com.microservice_rooms.service;

import com.microservice_rooms.entities.Room;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IServiceRoom {
    Room createRoom(Room room, Set<String> tagNames);
    Optional<Room> getRoomById(Long id);
    List<Room> getAllRooms();
    List<Room> findRoomsByTags(Set<String> tagNames);
    Room updateRoom(Long id, Room roomDetails, Set<String> tagNames);
    void deleteRoom(Long id);
    Room updateRoomStatus(Long id, Room.AvailabilityStatus newStatus);
}