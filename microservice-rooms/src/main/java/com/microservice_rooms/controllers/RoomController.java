package com.microservice_rooms.controllers;
import com.microservice_rooms.dto.RoomTypeDTO;
import com.microservice_rooms.entities.Room;
import com.microservice_rooms.entities.RoomType;
import com.microservice_rooms.entities.Tag;
import com.microservice_rooms.entities.RoomImage;
import com.microservice_rooms.service.IServiceFileStorage;
import com.microservice_rooms.service.IServiceRoom;
import com.microservice_rooms.service.ImplServiceFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.*;
//@CrossOrigin(origins = "http://localhost:3000")
@RestController
//@RequestMapping("/api/rooms")
@RequestMapping("/main")
public class RoomController {

    @Autowired
    private final IServiceRoom roomService;
    private final IServiceFileStorage fileStorage;

    public RoomController(IServiceRoom roomService, IServiceFileStorage fileStorage) {
        this.roomService = roomService;
        this.fileStorage= fileStorage;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Room> createRoom(
            @RequestPart("room") Room room,
            @RequestParam(value = "tags", required = false) Set<String> tags,
            @RequestParam(value = "images", required = false) MultipartFile[] images
    ) throws IOException {
        if (images != null) {
            for (MultipartFile img : images) {
                String filename = fileStorage.storefile(img);
                RoomImage ri = new RoomImage();
                ri.setFilename(filename);
                ri.setRoom(room);
                room.getImages().add(ri);
            }
        }
        Room saved = roomService.createRoom(room, tags != null ? tags : Set.of());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Room> updateRoom(
            @PathVariable Long id,
            @RequestPart("room") Room roomDetails,
            @RequestParam(value = "tags", required = false) Set<String> tags,
            @RequestParam(value = "newImages", required = false) MultipartFile[] newImages,
            @RequestParam(value = "removeImageIds", required = false) Set<Long> removeImageIds
    ) throws IOException {
        // Eliminar archivos físicos
        if (removeImageIds != null) {
            roomService.getRoomById(id).ifPresent(room ->
                    room.getImages().stream()
                            .filter(img -> removeImageIds.contains(img.getId()))
                            .forEach(img -> {
                                try { fileStorage.deletefile(img.getFilename()); } catch (IOException ignored) {}
                            })
            );
        }
        // Actualiza entidad (orphanRemoval borrará RoomImage huérfanas)
        Room updated = roomService.updateRoom(id, roomDetails, tags != null ? tags : Set.of());

        // Añadir nuevas imágenes
        if (newImages != null) {
            for (MultipartFile img : newImages) {
                String filename = fileStorage.storefile(img);
                RoomImage ri = new RoomImage();
                ri.setFilename(filename);
                ri.setRoom(updated);
                updated.getImages().add(ri);
            }
            updated = roomService.updateRoom(id, updated, tags != null ? tags : Set.of());
        }
        return ResponseEntity.ok(updated);
    }
    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.getRoomById(id).ifPresent(room ->
                room.getImages().forEach(img -> {
                    try { fileStorage.deletefile(img.getFilename()); } catch (IOException ignored) {}
                })
        );
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) throws IOException {
        Resource file = fileStorage.loadFileResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .body(file);
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