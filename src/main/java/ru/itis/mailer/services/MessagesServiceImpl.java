package ru.itis.mailer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.itis.mailer.dto.ContactInfoDto;
import ru.itis.mailer.dto.ContactKafkaDto;
import ru.itis.mailer.dto.MessageDto;
import ru.itis.mailer.dto.MessageKafkaDto;
import ru.itis.mailer.enums.ContactInfoType;
import ru.itis.mailer.enums.MessageState;
import ru.itis.mailer.models.*;
import ru.itis.mailer.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessagesServiceImpl implements MessagesService {

    @Autowired
    public MessageRepository messageRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public AddressBookRepository addressBookRepository;

    @Autowired
    public ContactInfoRepository contactInfoRepository;

    @Autowired
    public ContactRepository contactRepository;

    @Autowired
    private KafkaTemplate<String, MessageKafkaDto> kafkaTemplate;

    private final String TOPIC = "send-instructions";
    private final String TOPIC_TG = "send-instructions-tg";


    @Override
    public boolean sendCascadeMessage(List<MessageDto> messageDtoList) {
        boolean result = false;
        try {
            if (!messageDtoList.isEmpty()) {
                Optional<AddressBook> book = addressBookRepository.findById(messageDtoList.get(0).getAddressBookId());
                if (book.isPresent()) {
                    List<Contact> contacts = contactRepository.findAllByBook(book.get());
                    for (MessageDto messageDto : messageDtoList) {
                        List<ContactKafkaDto> kafkaContacts = new ArrayList<>();
                        for (Contact contact : contacts) {
                            ContactInfo contactInfo = contactInfoRepository.findByContact_IdAndType(contact.getId(), messageDto.getContactInfoType());
                            kafkaContacts.add(ContactKafkaDto.builder()
                                    .firstName(contact.getFirstName())
                                    .lastName(contact.getLastName())
                                    .bookName(book.get().getName())
                                    .address(contactInfo.getValue())
                                    .build()
                            );
                        }
                        kafkaTemplate.send(messageDto.getContactInfoType().getValue(), MessageKafkaDto.builder()
                                .body(messageDto.getBody())
                                .subject(messageDto.getSubject())
                                .type(messageDto.getContactInfoType())
                                .contacts(kafkaContacts)
                                .build()
                        );
                        System.out.println("Sent message: " + messageDto);
                        saveMessage(messageDto);
                    }
                    result = true;
                }
            }
            return result;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean sendMessage(MessageDto messageDto) {
        try {
            Optional<AddressBook> book = addressBookRepository.findById(messageDto.getAddressBookId());
            if (book.isPresent()) {
                List<Contact> contacts = contactRepository.findAllByBook(book.get());
                List<ContactKafkaDto> kafkaContacts = new ArrayList<>();
                for (Contact contact : contacts) {
                    ContactInfo contactInfo = contactInfoRepository.findByContact_IdAndType(contact.getId(), messageDto.getContactInfoType());
                    kafkaContacts.add(ContactKafkaDto.builder()
                            .firstName(contact.getFirstName())
                            .lastName(contact.getLastName())
                            .bookName(book.get().getName())
                            .address(contactInfo.getValue())
                            .build()
                    );
                }
                kafkaTemplate.send(messageDto.getContactInfoType().getValue(), MessageKafkaDto.builder()
                        .body(messageDto.getBody())
                        .subject(messageDto.getSubject())
                        .type(messageDto.getContactInfoType())
                        .contacts(kafkaContacts)
                        .build()
                );
                System.out.println("Sent message: " + messageDto);
                saveMessage(messageDto);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void saveMessage(MessageDto messageDto) {
        Optional<User> userOptional = userRepository.findById(messageDto.getCreatorId());
        Optional<AddressBook> addressBookOptional = addressBookRepository.findById(messageDto.getAddressBookId());
        if (userOptional.isPresent() && addressBookOptional.isPresent()) {
            User user = userOptional.get();
            AddressBook addressBook = addressBookOptional.get();
            messageRepository.save(Message.builder().
                    subject(messageDto.getSubject()).
                    body(messageDto.getBody())
                    .creator(user)
                    .messageTemplate(null)
                    .addressBook(addressBook)
                    .contactInfoType(messageDto.getContactInfoType())
                    .messageState(MessageState.CREATED)
                    .build());
        }
    }
}
