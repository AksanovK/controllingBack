package ru.itis.mailer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.mailer.dto.InstructionsResponse;
import ru.itis.mailer.models.AddressBook;
import ru.itis.mailer.models.User;
import ru.itis.mailer.repositories.AddressBookRepository;
import ru.itis.mailer.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InstructionsServiceImpl implements InstructionsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressBookRepository addressBookRepository;

    @Override
    public InstructionsResponse getInstructions(Long userId) {
        Optional<User> user = userRepository.findById(userId.intValue());
        if (user.isPresent()) {
            List<AddressBook> addressBooks = addressBookRepository.findByCreator(user.get());
            List<String> messengers = List.of("EMAIL", "TELEGRAM", "VK", "WHATSAPP", "DISCORD");
            return InstructionsResponse.builder().
                    messengers(messengers).
                    books(addressBooks).
                    build();

        }
        return new InstructionsResponse();
    }
}
