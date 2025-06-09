package com.MicroserviceReservation.persistence;

import com.MicroserviceReservation.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface userRepository extends JpaRepository<User, Long> {

}
