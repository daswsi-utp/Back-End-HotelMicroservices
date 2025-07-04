package com.microservice.users.microservice_users.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

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
    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
    @ManyToMany
    @JoinTable(
            name="user_roles",
            joinColumns = {@JoinColumn(name="user_id")},inverseJoinColumns = {@JoinColumn(name="role_id")},
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","role_id"})}
    )
    private List<Role> roles;

    private Boolean enabled;

    @Transient
    @JsonProperty
    private Boolean admin;

    public Boolean isEnabled() {
        return enabled;
    }


    public Boolean isAdmin() {
        return admin;
    }

}