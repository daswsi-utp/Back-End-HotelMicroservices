package com.microservice_rooms.controllers;


import com.microservice_rooms.entities.RoomType;
import com.microservice_rooms.service.IServiceRoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/roomtype")

public class RoomTypeController {

    @Autowired
    private IServiceRoomType serviceRoomType;

    @GetMapping
    public List<RoomType> getAllRoomTypes(){
        return serviceRoomType.getAllRoomsTypes();
    }
}
