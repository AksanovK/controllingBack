package ru.itis.mailer.controllers;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.mailer.dto.AddressBookDto;
import ru.itis.mailer.dto.ContactDto;
import ru.itis.mailer.dto.InstructionsResponse;
import ru.itis.mailer.services.AddressBooksService;
import ru.itis.mailer.services.ContactsService;
import ru.itis.mailer.services.InstructionsService;

import java.util.List;
import java.util.Map;

@RequestMapping("/addressBooks")
@RestController
public class AddressBooksController {

    @Autowired
    private AddressBooksService addressBooksService;

    @Autowired
    private ContactsService contactsService;

    @GetMapping("/getListOfBooks")
    public ResponseEntity<List<AddressBookDto>> getAddressBooksByUserId(@RequestParam Long userId) {
        return ResponseEntity.ok(addressBooksService.getAddressBooksByUser(userId));
    }

    @GetMapping("/getContactsByBookId")
    public ResponseEntity<List<ContactDto>> getContactsByBookId(@RequestParam Long bookId) {
        return ResponseEntity.ok(contactsService.getContactsByBook(bookId));
    }

    @PostMapping("/addAddressBook")
    public ResponseEntity<?> addAddressBook(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Файл не был получен или пуст");
        }
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.endsWith(".csv")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Файл должен быть в формате CSV");
            }
            addressBooksService.saveAddressBook(file, userId);
            return ResponseEntity.ok("Файл успешно загружен и обработан: " + fileName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обработке файла: " + e.getMessage());
        }
    }

    @PostMapping("/renameAddressBook")
    public ResponseEntity<?> renameAddressBook(@RequestParam("name") String name, @RequestParam("bookId") Long bookId) {
        addressBooksService.renameAddressBook(bookId, name);
        return ResponseEntity.ok("Книга успешно переименована");
    }

    @Transactional
    @PostMapping("/deleteAddressBook")
    public ResponseEntity<?> deleteAddressBook(@RequestParam("bookId") Long bookId) {
        addressBooksService.deleteAddressBook(bookId);
        return ResponseEntity.ok("Книга успешно удалена");
    }
}
