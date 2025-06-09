package com.microservice.users.microservice_users.service;

import com.microservice.users.microservice_users.entities.User;
import com.microservice.users.microservice_users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

    @Service
    public class UserServiceImpl implements IUserService
    {
        @Autowired
        private UserRepository userRepository;


        @Autowired
        private PasswordEncoder passwordEncoder;


        @Override
        @Transactional(readOnly = true)
        public List<User> findAll() {
            return (List<User>) userRepository.findAll();
        }

        @Override
        @Transactional(readOnly = true)
        public Optional<User> findById(Long id) {
            return userRepository.findById(id);
        }

        @Override
        @Transactional(readOnly = true)
        public Optional<User> findByEmail(String email) {
            return userRepository.findByEmail(email);
        }

        @Override
        @Transactional
        public User save(User user) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }




        @Override
        @Transactional
        public void deleteById(Long id) {
            userRepository.deleteById(id);
        }


        @Override
        @Transactional
        public Optional<User> update(User user, Long id) {
            Optional<User> userOptional = this.findById(id);
            return userOptional.map(usDb ->{

                usDb.setEmail(user.getEmail());
                usDb.setName(user.getName());
                usDb.setCity(user.getCity());
                usDb.setCountry(user.getCountry());
                usDb.setLastName(user.getLastName());
                return Optional.of(userRepository.save(usDb));
            }).orElseGet(()->Optional.empty());
        }




    }
