package ru.itis.mailer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.mailer.dto.ContactMessageDto;
import ru.itis.mailer.dto.MessageDto;
import ru.itis.mailer.services.MessagesService;

import java.util.List;

@RequestMapping("/message")
@RestController
public class MessageController {

    @Autowired
    public MessagesService messagesService;

    @PostMapping("/save")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto messageDto) {
        boolean success = messagesService.sendMessage(messageDto);
        if (success) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message");
        }
    }

    @PostMapping("/send/cascade")
    public ResponseEntity<?> sendCascadeMessage(@RequestBody List<MessageDto> messageDtoList) {
        boolean success = messagesService.sendCascadeMessage(messageDtoList);
        if (success) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message");
        }
    }

    @PostMapping("/send/contacts")
    public ResponseEntity<?> sendContactsMessage(@RequestBody ContactMessageDto messageDto) {
        boolean success = messagesService.sendContactsMessage(messageDto);
        if (success) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message");
        }
    }
}
