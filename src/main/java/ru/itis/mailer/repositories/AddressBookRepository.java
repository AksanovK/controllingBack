package ru.itis.mailer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.mailer.models.AddressBook;
import ru.itis.mailer.models.Token;
import ru.itis.mailer.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressBookRepository extends JpaRepository<AddressBook, Long> {
    List<AddressBook> findByCreator(User user);
}
