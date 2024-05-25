package ru.itis.mailer.services;

import org.springframework.data.domain.Page;
import ru.itis.mailer.dto.AddressBookBaseDto;
import ru.itis.mailer.dto.ContactDto;
import ru.itis.mailer.models.Contact;

import java.util.List;

public interface ContactsService {
    void deleteContactById(Long contactId);
    ContactDto getContactById(Long id);
    List<ContactDto> getContactsByAddressBookId(Long addressBookId);
    List<ContactDto> getContactsByBook(Long addressBookId);
    Page<ContactDto> getContactsPaginated(Long bookId, Integer page, Integer pageSize);
    Page<ContactDto> searchContacts(Long bookId, Integer page, Integer pageSize, String search);
}
