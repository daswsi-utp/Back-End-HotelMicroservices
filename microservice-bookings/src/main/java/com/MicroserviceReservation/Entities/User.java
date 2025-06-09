package com.MicroserviceReservation.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    private Long idUser;
    private String Fullname;
    private String Contact;
}