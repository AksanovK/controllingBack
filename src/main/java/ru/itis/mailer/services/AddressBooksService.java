package ru.itis.mailer.services;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.mailer.dto.AddressBookBaseDto;
import ru.itis.mailer.dto.AddressBookDto;
import ru.itis.mailer.dto.ContactDto;

import java.util.List;

public interface AddressBooksService {
    void saveAddressBook(MultipartFile file, Long userId);
    void deleteAddressBook(Long bookId);
    List<AddressBookDto> getAddressBooksByUser(Long userId);

    void renameAddressBook(Long bookId, String newName);
}
