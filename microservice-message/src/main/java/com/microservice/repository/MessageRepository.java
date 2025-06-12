package com.microservice.microservice_message.repository;

import com.microservice.microservice_message.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
