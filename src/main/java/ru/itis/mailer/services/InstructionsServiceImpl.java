package ru.itis.mailer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.mailer.dto.AddressBookDto;
import ru.itis.mailer.dto.InstructionsResponse;
import ru.itis.mailer.models.AddressBook;
import ru.itis.mailer.models.User;
import ru.itis.mailer.repositories.AddressBookRepository;
import ru.itis.mailer.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InstructionsServiceImpl implements InstructionsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressBookRepository addressBookRepository;

    @Override
    public InstructionsResponse getInstructions(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            //TODO: only active
            List<AddressBookDto> addressBooks = addressBookRepository.findAddressBooksByCreator(user.get());
            List<String> messengers = List.of("EMAIL", "TELEGRAM", "VK", "WHATSAPP", "DISCORD");
            return InstructionsResponse.builder().
                    messengers(messengers).
                    books(addressBooks).
                    build();

        }
        return new InstructionsResponse();
    }
}
