package com.microservice_rooms.service;

import com.microservice_rooms.entities.RoomType;
import com.microservice_rooms.persistence.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImplServiceRoomType implements IServiceRoomType{

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Override
    public List<RoomType> getAllRoomsTypes() {
        return roomTypeRepository.findAll();
    }
}
