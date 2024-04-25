package ru.itis.mailer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.mailer.dto.MessageDto;
import ru.itis.mailer.services.MessagesService;

@RequestMapping("/message")
@RestController
public class MessageController {

    @Autowired
    public MessagesService messagesService;

    @PostMapping("/save")
    public ResponseEntity<?> getAddressBooksByUserId(@RequestBody MessageDto messageDto) {
        messagesService.saveMessage(messageDto);
        return ResponseEntity.ok("success");
    }
}
