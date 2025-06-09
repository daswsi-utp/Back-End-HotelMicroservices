package com.microservice.users.microservice_users.service;

import com.microservice.users.microservice_users.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService
{
          List<User> findAll();
          Optional<User> findById(Long id);
          Optional<User> findByEmail(String email);
          User save(User user);
          Optional<User> update(User user, Long id);
          void deleteById(Long id);

}
