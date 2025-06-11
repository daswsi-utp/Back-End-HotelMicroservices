package com.microservice_rooms.controller;

import com.microservice_rooms.entities.Room;
import com.microservice_rooms.service.IRoomService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private IRoomService roomService;

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.findAll();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        return roomService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@Validated @RequestBody Room room) {
        Room savedRoom = roomService.save(room);
        URI location = URI.create(String.format("/api/rooms/%s", savedRoom.getId()));
        return ResponseEntity.created(location).body(savedRoom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@Validated @RequestBody Room room, @PathVariable Long id) {
        try {
            Room updatedRoom = roomService.update(room, id);
            return ResponseEntity.ok(updatedRoom);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        if (roomService.findById(id).isPresent()) {
            roomService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
