package ru.itis.mailer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.mailer.dto.AddressBookDto;
import ru.itis.mailer.dto.ContactDto;
import ru.itis.mailer.dto.UserLoginDto;
import ru.itis.mailer.services.AddressBooksService;
import ru.itis.mailer.services.ContactsService;

import java.util.List;
import java.util.Map;

@RequestMapping("/contacts")
@RestController
public class ContactsController {

    @Autowired
    private ContactsService contactsService;

    @PostMapping("/deleteContact")
    public ResponseEntity<?> getAddressBooksByUserId(@RequestBody Map<String, Long> payload) {
        Long contactId = payload.get("contactId");
        contactsService.deleteContactById(contactId);
        return ResponseEntity.ok("");
    }

    @GetMapping("/getContactPage")
    public ResponseEntity<ContactDto> getContactsByBookId(@RequestParam Long contactId) {
        return ResponseEntity.ok(contactsService.getContactById(contactId));
    }

}
