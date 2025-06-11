package com.microservice_rooms.service;

import com.microservice_rooms.entities.Room;
import com.microservice_rooms.repositories.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements IRoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Room> findAll() {
        return (List<Room>) roomRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Room> findById(Long id) {
        return roomRepository.findById(id);
    }

    @Override
    @Transactional
    public Room save(Room room) {
        return roomRepository.save(room);
    }

    @Override
    @Transactional
    public Room update(Room room, Long id) {
        return roomRepository.findById(id).map(existingRoom -> {
            existingRoom.setNameRoom(room.getNameRoom());
            return roomRepository.save(existingRoom);
        }).orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        roomRepository.deleteById(id);
    }
}


