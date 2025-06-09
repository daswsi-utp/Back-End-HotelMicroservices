package com.MicroserviceReservation.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @Column(name = "room_id")
    private Long idRoom;
    private String name;
    private String room_type;
    private String description;
    private Double price_per_night;
    private int capacity;


}