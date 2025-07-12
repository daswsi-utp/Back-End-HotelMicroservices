package com.microservice_rooms.controllers;
import com.microservice_rooms.dto.RoomTypeDTO;
import com.microservice_rooms.entities.Room;
import com.microservice_rooms.entities.Tag;
import com.microservice_rooms.entities.RoomImage;
import com.microservice_rooms.service.ILocalFileCacheService;
import com.microservice_rooms.service.IS3Service;
import com.microservice_rooms.service.IServiceRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private final IServiceRoom roomService;
    private final ILocalFileCacheService fileStorage;
    private final IS3Service s3Service;

    public RoomController(IServiceRoom roomService, ILocalFileCacheService fileStorage, IS3Service s3Service) {
        this.roomService = roomService;
        this.fileStorage= fileStorage;
        this.s3Service = s3Service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Room> createRoom(
            @RequestPart("room") Room room,
            @RequestParam(value = "tags", required = false) Set<String> tags,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestParam(value = "mainImageIndex", required = false) Integer mainImageIndex
    ) throws IOException {
        if (images != null) {
            for (int i = 0; i < images.length; i++) {
                MultipartFile img = images[i];
                //String filename = fileStorage.storefile(img);
                String filename = UUID.randomUUID() + "-" + img.getOriginalFilename();
                Path tempFile = Files.createTempFile("upload-", filename);
                Files.write(tempFile, img.getBytes());

                boolean uploaded = s3Service.uploadFile("hotel-room-images-utp", filename, tempFile);
                Files.delete(tempFile);
                if(!uploaded){
                    throw new IOException("Failed to upload file to S3");
                }
                RoomImage ri = new RoomImage();
                ri.setFilename(filename);
                ri.setRoom(room);
                boolean isMain = false;
                if (mainImageIndex != null) {
                    isMain = mainImageIndex == i;
                } else if (images.length == 1) {
                    isMain = true; // Si solo hay una imagen, será la principal automáticamente
                }
                ri.setMain(isMain);
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
            @RequestParam(value = "removeImageIds", required = false) Set<Long> removeImageIds,
            @RequestParam(value = "mainImageIndex", required = false) Integer mainImageIndex,
            @RequestParam(value = "mainImageId", required = false) Long mainImageId
    ) throws IOException {

        // Obtener y actualizar imágenes eliminadas (tanto archivo como entidad)
        if (removeImageIds != null && !removeImageIds.isEmpty()) {
            Optional<Room> optionalRoom = roomService.getRoomById(id);
            if (optionalRoom.isPresent()) {
                Room room = optionalRoom.get();

                // Eliminar archivos físicos
                room.getImages().stream()
                        .filter(img -> removeImageIds.contains(img.getId()))
                        .forEach(img -> {
                            try {
                                fileStorage.deletefile(img.getFilename());
                            } catch (IOException ignored) {}
                        });

                // Remover entidades de imagen (para que JPA las borre)
                room.getImages().removeIf(img -> removeImageIds.contains(img.getId()));

                // Guardar la eliminación de las imágenes
                roomService.updateRoom(id, room, tags != null ? tags : Set.of());
            }
        }

        // Actualiza los datos generales de la habitación
        Room updated = roomService.updateRoom(id, roomDetails, tags != null ? tags : Set.of());

        // Añadir nuevas imágenes
        if (newImages != null && newImages.length > 0) {
            // Desmarcar todas como principal
            updated.getImages().forEach(img -> img.setMain(false));

            // Añadir nuevas
            for (int i = 0; i < newImages.length; i++) {
                MultipartFile img = newImages[i];
                //String filename = fileStorage.(img);
                String filename = UUID.randomUUID() + "-" + img.getOriginalFilename();
                Path tempFile = Files.createTempFile("upload-", filename);
                Files.write(tempFile, img.getBytes());

                boolean uploaded = s3Service.uploadFile("hotel-room-images-utp", filename, tempFile);
                Files.delete(tempFile);
                if(!uploaded){
                    throw new IOException("Failed to upload file to S3");
                }
                RoomImage ri = new RoomImage();
                ri.setFilename(filename);
                ri.setRoom(updated);
                boolean isMain = mainImageIndex != null && mainImageIndex == i;
                ri.setMain(isMain);
                updated.getImages().add(ri);
            }

            updated = roomService.updateRoom(id, updated, tags != null ? tags : Set.of());

        } else if (mainImageId != null) {
            // Cambiar imagen principal existente
            for (RoomImage img : updated.getImages()) {
                img.setMain(img.getId().equals(mainImageId));
            }

            updated = roomService.updateRoom(id, updated, tags != null ? tags : Set.of());
        }

        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<Room> updateRoomStatus(
            @PathVariable Long id,
            @RequestParam("status") Room.AvailabilityStatus status
    ) {
        try {
            Room updatedRoom = roomService.updateRoomStatus(id, status);
            return ResponseEntity.ok(updatedRoom);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
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
                    try {
                        fileStorage.deletefile(img.getFilename());
                    } catch (IOException ignored) {}

                })
        );
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) throws IOException {
        Resource file = fileStorage.loadOrDownload("hotel-room-images-utp", filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
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

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalRoomsCount() {
        return ResponseEntity.ok(roomService.countRooms());
    }
}