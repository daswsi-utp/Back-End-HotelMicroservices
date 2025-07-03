package com.microservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.microservice.dto.MessageDTO;
import com.microservice.model.Message;
import com.microservice.service.MessageService;

@Controller
public class ChatController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(MessageDTO dto) {
        // ✅ Guarda el mensaje y envía correos
        Message saved = messageService.saveFromDTO(dto);

        // ✅ Envío en tiempo real a los suscriptores WebSocket
        messagingTemplate.convertAndSend("/topic/messages", saved);
    }
}
