package com.microservice_rooms.entities;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameRoom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameRoom() {
        return nameRoom;
    }

    public void setNameRoom(String nameRoom) {
        this.nameRoom = nameRoom;
    }
}
