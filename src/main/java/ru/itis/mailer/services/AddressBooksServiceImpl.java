package ru.itis.mailer.services;

import jakarta.transaction.Transactional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.mailer.dto.AddressBookDto;
import ru.itis.mailer.enums.AddressBookState;
import ru.itis.mailer.enums.ContactInfoType;
import ru.itis.mailer.enums.GenderEnum;
import ru.itis.mailer.models.AddressBook;
import ru.itis.mailer.models.Contact;
import ru.itis.mailer.models.ContactInfo;
import ru.itis.mailer.models.User;
import ru.itis.mailer.repositories.AddressBookRepository;
import ru.itis.mailer.repositories.ContactInfoRepository;
import ru.itis.mailer.repositories.ContactRepository;
import ru.itis.mailer.repositories.UserRepository;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressBooksServiceImpl implements AddressBooksService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressBookRepository addressBookRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactInfoRepository contactInfoRepository;

    @Override
    @Transactional
    public void saveAddressBook(MultipartFile file, Long userId) {
        try {
            Optional<User> creator = userRepository.findById(userId);
            if (creator.isPresent()) {
                AddressBook addressBook = AddressBook.builder()
                        .name(getFilenameWithoutExtension(file.getOriginalFilename()))
                        .description("-")
                        .state(AddressBookState.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .countOfContacts(0)
                        .creator(creator.get())
                        .build();
                AddressBook savedAddressBook = addressBookRepository.save(addressBook);
                int countOfContacts = 0;
                try (CSVParser csvParser = new CSVParser(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8), CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
                    List<Contact> contacts = new ArrayList<>();
                    List<ContactInfo> contactInfoTypes = new ArrayList<>();
                    for (CSVRecord record : csvParser) {
                        Contact contact = Contact.builder()
                                .firstName(record.get("Имя"))
                                .lastName(record.get("Фамилия"))
                                .isForeigner(false)
                                .gender(mapGender(record.get("Пол")))
                                .birthday(Date.valueOf(LocalDate.parse(record.get("Дата рождения"), DateTimeFormatter.ISO_LOCAL_DATE)))
                                .additionalInfo(record.get("Дополнительная информация"))
                                .website("csv")
                                .book(savedAddressBook)
                                .createdAt(LocalDateTime.now())
                                .build();
                        contacts.add(contact);
                        Contact savedContact = contactRepository.save(contact);
                        countOfContacts++;
                        addContactInfo(contactInfoTypes, savedContact, ContactInfoType.EMAIL, record.get("EMAIL"));
                        addContactInfo(contactInfoTypes, savedContact, ContactInfoType.TELEGRAM, record.get("TELEGRAM"));
                        addContactInfo(contactInfoTypes, savedContact, ContactInfoType.VK, record.get("VK"));
                        addContactInfo(contactInfoTypes, savedContact, ContactInfoType.WHATSAPP, record.get("WHATSAPP"));
                    }
                    savedAddressBook.setCountOfContacts(countOfContacts);
                    addressBookRepository.save(savedAddressBook);
                    contactInfoRepository.saveAll(contactInfoTypes);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обработке файла: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteAddressBook(Long bookId) {
        Optional<AddressBook> addressBook = addressBookRepository.findById(bookId);
        if (addressBook.isPresent()) {
            List<Contact> contacts = contactRepository.findAllByBook(addressBook.get());
            for (Contact contact : contacts) {
                contactInfoRepository.deleteAllByContact_Id(contact.getId());
            }
            contactRepository.deleteAllByBook_Id(bookId);
            addressBookRepository.deleteById(bookId);
        }
    }

    private String getFilenameWithoutExtension(String filename) {
        if (filename == null) return null;
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(0, dotIndex);
        }
        return filename;
    }

    private GenderEnum mapGender(String gender) {
        if (gender == null) return null;
        return switch (gender.trim()) {
            case "Мужской" -> GenderEnum.MALE;
            case "Женский" -> GenderEnum.FEMALE;
            default -> throw new IllegalArgumentException("Неизвестное значение пола: " + gender);
        };
    }


    private void addContactInfo(List<ContactInfo> contactInfoTypes, Contact contact, ContactInfoType type, String value) {
        if (value != null && !value.trim().isEmpty()) {
            ContactInfo contactInfo = ContactInfo.builder()
                    .contact(contact)
                    .type(type)
                    .value(value)
                    .createdAt(LocalDateTime.now())
                    .build();

            contactInfoTypes.add(contactInfo);
        }
    }

    @Override
    public List<AddressBookDto> getAddressBooksByUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return addressBookRepository.findAddressBooksByCreator(user.get());
        }
        return new ArrayList<>();
    }

    @Override
    public void renameAddressBook(Long bookId, String newName) {
        AddressBook addressBook = addressBookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("AddressBook not found with id: " + bookId));
        addressBook.setName(newName);
        addressBookRepository.save(addressBook);
    }
}
