package com.microservice.oauth.microservice_oauth.models;




import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.List;


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

}
