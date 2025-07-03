package com.microservice.users.microservice_users.service;

import com.microservice.users.microservice_users.entities.Role;
import com.microservice.users.microservice_users.entities.User;
import com.microservice.users.microservice_users.repositories.RoleRepository;
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
        private RoleRepository roleRepository;

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
            user.setEnabled(true);
            user.setRoles(getRoles(user));
            return userRepository.save(user);
        }
        @Override
        @Transactional
        public void deleteById(Long id) {
            userRepository.deleteById(id);
        }

        @Override
        public Optional<User> findByUsername(String username) {
            return userRepository.findByUsername(username);
        }

        @Override
        public Long countUsers() {
            return userRepository.count();
        }


        @Override
        @Transactional
        public Optional<User> update(User user, Long id) {
            Optional<User> userOptional = this.findById(id);
            return userOptional.map(usDb ->{

                usDb.setEmail(user.getEmail());
                usDb.setName(user.getName());
                usDb.setSecondName(user.getSecondName());
                usDb.setLastName(user.getLastName());
                usDb.setCellPhone(user.getCellPhone());
                usDb.setPassword(user.getPassword());
                if (user.getAdmin() != null)
                    usDb.setAdmin(user.getAdmin());
                if(user.isEnabled()==null)
                        usDb.setEnabled(true);
                else
                    usDb.setEnabled(user.isEnabled());
                return Optional.of(userRepository.save(usDb));
            }).orElseGet(()->Optional.empty());
        }
        private List<Role> getRoles(User  user) {
            List<Role> roles = new ArrayList<>();
            Optional<Role> roleOptional = roleRepository.findByName("ROLE_USER");
            roleOptional.ifPresent(roles::add); //otra forma
            if(Boolean.TRUE.equals(user.isAdmin())) {
                Optional<Role> adminRoleOptional = roleRepository.findByName("ROLE_ADMIN");
                adminRoleOptional.ifPresent(roles::add);
            }
            return roles;
        }




    }
