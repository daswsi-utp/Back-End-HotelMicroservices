package com.microservice.microservice_message.service;

import com.microservice.microservice_message.dto.MessageDTO;
import com.microservice.microservice_message.model.Message;
import com.microservice.microservice_message.repository.MessageRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository repository;
    private final EmailService emailService;

    public MessageService(MessageRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    @CacheEvict(value = "messages", allEntries = true)
    public Message saveFromDTO(MessageDTO dto) {
        Message message = new Message();
        message.setName(dto.getName());
        message.setEmail(dto.getEmail());
        message.setSubject(dto.getSubject());
        message.setContent(dto.getContent());
        message.setRead(false);

        Message savedMessage = repository.save(message);

        // Enviar correo al usuario
        String userEmail = message.getEmail();
        String userSubject = "Confirmación de recepción de tu mensaje";
        String userText = "Hola " + message.getName() + ",\n\n" +
                          "Hemos recibido tu mensaje con el asunto: '" + message.getSubject() + "'.\n" +
                          "Pronto nos pondremos en contacto contigo.\n\n" +
                          "Gracias por escribirnos.\n\nHotel Paradise";
        emailService.sendSimpleMessage(userEmail, userSubject, userText);

        // Enviar notificación al admin
        String adminEmail = "waberto113@gmail.com";
        String adminSubject = "Nuevo mensaje recibido de " + message.getName();
        String adminText = "Has recibido un nuevo mensaje:\n\n" +
                           "Nombre: " + message.getName() + "\n" +
                           "Email: " + message.getEmail() + "\n" +
                           "Asunto: " + message.getSubject() + "\n" +
                           "Mensaje: " + message.getContent();
        emailService.sendSimpleMessage(adminEmail, adminSubject, adminText);

        return savedMessage;
    }

    @Cacheable(value = "messages")
    public List<Message> findAll() {
        return repository.findAll();
    }

    @CacheEvict(value = "messages", allEntries = true)
    public Message markMessageAsRead(Long id) {
        return repository.findById(id).map(message -> {
            message.setRead(true);
            return repository.save(message);
        }).orElse(null);
    }
}
