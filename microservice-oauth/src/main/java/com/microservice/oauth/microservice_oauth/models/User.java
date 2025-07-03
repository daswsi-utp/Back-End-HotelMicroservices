package com.microservice.oauth.microservice_oauth.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class User
{

    private Long id;
    private String name;
    private String secondName;
    private String lastName;
    private String username;

    private String email;
    private String cellPhone;

    private String password;

    private List<Role> roles;

    private Boolean enabled;


    private Boolean admin;
    public Boolean isEnabled() {
        return enabled;
    }
    public Boolean isAdmin() {
        return admin;
    }
}
