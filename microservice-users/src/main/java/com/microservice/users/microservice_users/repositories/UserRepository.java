package com.microservice.users.microservice_users.repositories;

import com.microservice.users.microservice_users.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long>
{
    Optional<User> findByEmail(String email);
}
