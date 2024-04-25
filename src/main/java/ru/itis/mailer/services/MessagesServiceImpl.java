package ru.itis.mailer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.mailer.dto.MessageDto;
import ru.itis.mailer.enums.MessageState;
import ru.itis.mailer.models.AddressBook;
import ru.itis.mailer.models.Message;
import ru.itis.mailer.models.User;
import ru.itis.mailer.repositories.AddressBookRepository;
import ru.itis.mailer.repositories.MessageRepository;
import ru.itis.mailer.repositories.UserRepository;

import java.util.Optional;

@Service
public class MessagesServiceImpl implements MessagesService {

    @Autowired
    public MessageRepository messageRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public AddressBookRepository addressBookRepository;

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
