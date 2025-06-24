package com.microservice_rooms.controllers;
import com.microservice_rooms.dto.RoomTypeDTO;
import com.microservice_rooms.entities.Room;
import com.microservice_rooms.entities.RoomType;
import com.microservice_rooms.entities.Tag;
import com.microservice_rooms.service.IServiceRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
//@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private final IServiceRoom roomService;

    public RoomController(IServiceRoom roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room, @RequestParam Set<String> tags) {
        Room createdRoom = roomService.createRoom(room, tags);
        return ResponseEntity.ok(createdRoom);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id,
                                           @RequestBody Room roomDetails,
                                           @RequestParam Set<String> tags) {
        Room updatedRoom = roomService.updateRoom(id, roomDetails, tags);
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<Room> findRoomsByTags(@RequestParam Set<String> tags) {
        return roomService.findRoomsByTags(tags);
    }
    @GetMapping("/type/{id}")
    public RoomTypeDTO getRoomTypeById(@PathVariable Long id){
        return roomService.getRoomTypeDTOById(id);
    }
    @GetMapping("/type/all")
    public List<RoomTypeDTO> getAllRoomTypes(){
        return roomService.getAllRoomTypesDTO();
    }

    @GetMapping("/room/Detail")
    public List<Tag> getAllTags() {
        return roomService.getAllTags();
    }

    @GetMapping("/room/Detail/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable Long id) {
        Optional<Tag> tag = roomService.getTagById(id);
        return tag.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}