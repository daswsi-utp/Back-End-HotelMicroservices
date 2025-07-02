package com.microservice_rooms.service;
import com.microservice_rooms.entities.Room;
import com.microservice_rooms.entities.RoomImage;
import com.microservice_rooms.entities.Tag;
import com.microservice_rooms.persistence.RoomRepository;
import com.microservice_rooms.persistence.TagRepository;
import org.springframework.data.domain.Sort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;
@Service
@Transactional
public class ImplServiceRoom implements IServiceRoom{

    private final RoomRepository roomRepository;
    private final TagRepository tagRepository;

    public ImplServiceRoom(RoomRepository roomRepository, TagRepository tagRepository) {
        this.roomRepository = roomRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public Room createRoom(Room room, Set<String> tagNames) {
        if (room.getImages() != null) {
            for (RoomImage image : room.getImages()) {
                image.setRoom(room); // <- Esto es CLAVE
            }
        }

        Set<Tag> tags = getOrCreateTags(tagNames);
        room.setTags(tags);
        return roomRepository.save(room);
    }

    @Override
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll(Sort.by("roomId").ascending());
    }

    @Override
    public List<Room> findRoomsByTags(Set<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return Collections.emptyList();
        }

        // Buscar etiquetas que existen
        List<Tag> foundTags = tagRepository.findAll().stream()
                .filter(tag -> tagNames.contains(tag.getName()))
                .collect(Collectors.toList());

        if (foundTags.isEmpty()) {
            return Collections.emptyList();
        }

        // Buscar habitaciones que tengan alguna de esas etiquetas
        return roomRepository.findAll().stream()
                .filter(room -> room.getTags().stream().anyMatch(foundTags::contains))
                .collect(Collectors.toList());
    }

    @Override
    public Room updateRoom(Long id, Room roomDetails, Set<String> tagNames) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Room no encontrada"));

        existingRoom.setRoomNumber(roomDetails.getRoomNumber());
        existingRoom.setPricePerNight(roomDetails.getPricePerNight());
        existingRoom.setAvailabilityStatus(roomDetails.getAvailabilityStatus());
        existingRoom.setCapacity(roomDetails.getCapacity());
        existingRoom.setRoomSize(roomDetails.getRoomSize());
        existingRoom.setDescription(roomDetails.getDescription());
        existingRoom.setRoomType(roomDetails.getRoomType());

        Set<Tag> tags = getOrCreateTags(tagNames);
        existingRoom.setTags(tags);

        return roomRepository.save(existingRoom);
    }

    @Override
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    private Set<Tag> getOrCreateTags(Set<String> tagNames) {
        Set<Tag> tags = new HashSet<>();
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setName(tagName);
                        return tagRepository.save(newTag);
                    });
            tags.add(tag);
        }
        return tags;
    }
}
