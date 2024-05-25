package ru.itis.mailer.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.itis.mailer.dto.AddressBookDto;
import ru.itis.mailer.dto.ContactDto;
import ru.itis.mailer.dto.ContactInfoDto;
import ru.itis.mailer.models.AddressBook;
import ru.itis.mailer.models.Contact;
import ru.itis.mailer.models.ContactInfo;
import ru.itis.mailer.repositories.AddressBookRepository;
import ru.itis.mailer.repositories.ContactInfoRepository;
import ru.itis.mailer.repositories.ContactRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactsServiceImpl implements ContactsService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactInfoRepository contactInfoRepository;


    @Autowired
    private AddressBookRepository addressBookRepository;

    @Override
    @Transactional
    public void deleteContactById(Long contactId) {
        contactInfoRepository.deleteAllByContact_Id(contactId);
        contactRepository.deleteById(contactId);
    }

    @Override
    public ContactDto getContactById(Long contactId) {
        Optional<Contact> contact = contactRepository.findById(contactId);
        return contact.map(this::convertToDto).orElse(null);
    }

    @Override
    public List<ContactDto> getContactsByBook(Long addressBookId) {
        Optional<AddressBook> book = addressBookRepository.findById(addressBookId);
        if (book.isPresent()) {
            List<Contact> contacts = contactRepository.findAllByBook(book.get());
            return contacts.stream().map(this::convertToDto).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public Page<ContactDto> getContactsPaginated(Long bookId, Integer page, Integer pageSize) {
        try {
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<Contact> contactPage;

            if (bookId == 0) {
                contactPage = contactRepository.findAll(pageable);
            } else {
                contactPage = contactRepository.findByBookId(bookId, pageable);
            }
            System.out.println("Contacts found: " + contactPage.getTotalElements());
            System.out.println(contactPage.map(this::convertToDto));
            return contactPage.map(this::convertToDto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    @Transactional
    public Page<ContactDto> searchContacts(Long bookId, Integer page, Integer pageSize, String searchQuery) {
        Pageable pageable = PageRequest.of(page, pageSize);
        try {
            Page<Contact> contactPage;
            if (bookId == 0) {
                if (!searchQuery.isBlank()) {
                    contactPage = contactRepository.findByFirstNameContainingIgnoreCase(searchQuery, pageable);
                } else {
                    contactPage = contactRepository.findAll(pageable);
                }
            } else {
                if (!searchQuery.isBlank()) {
                    contactPage = contactRepository.findByBookIdAndFirstNameContainingIgnoreCase(bookId, searchQuery, pageable);
                } else {
                    contactPage = contactRepository.findByBookId(bookId, pageable);
                }
            }
            System.out.println("Contacts found: " + contactPage.getTotalElements());
            System.out.println(contactPage.map(this::convertToDto));
            return contactPage.map(this::convertToDto);
        } catch (Exception e) {
            System.err.println("Error during searchContacts: " + e.getMessage());
            return Page.empty();
        }
    }

    private ContactInfoDto convertContactInfoToDto(ContactInfo contactInfo) {
        return new ContactInfoDto(
                contactInfo.getId(),
                contactInfo.getType(),
                contactInfo.getValue()
        );
    }

    private ContactDto convertToDto(Contact contact) {
        List<ContactInfo> contactInfos = contactInfoRepository.findAllByContactId(contact.getId());
        List<ContactInfoDto> contactInfoDtos = contactInfos.stream()
                .map(this::convertContactInfoToDto)
                .collect(Collectors.toList());
        return new ContactDto(contact.getId(), contact.getFirstName(), contact.getLastName(),
                contact.getGender(), contact.getBirthday(), contact.getBook().getId(),
                contact.getAdditionalInfo(), contactInfoDtos, contact.getWebsite());
    }

    @Override
    public List<ContactDto> getContactsByAddressBookId(Long addressBookId) {
        Optional<AddressBook> book = addressBookRepository.findById(addressBookId);
        return book.isPresent() ? contactRepository.customFindAllByBook(book.get()) :  new ArrayList<>();
    }

}
