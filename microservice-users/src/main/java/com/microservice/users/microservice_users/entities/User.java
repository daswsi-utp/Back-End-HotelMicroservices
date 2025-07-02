package com.microservice.users.microservice_users.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String secondName;
    private String lastName;
    @Column(unique = true)
    private String username;
    @Email
    @NotBlank
    private String email;
    private String cellPhone;
    @NotBlank
    private String password;

}
