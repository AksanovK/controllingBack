package ru.itis.mailer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.mailer.dto.AddressBookDto;
import ru.itis.mailer.models.AddressBook;
import ru.itis.mailer.models.User;

import java.util.List;

@Repository
public interface AddressBookRepository extends JpaRepository<AddressBook, Long> {
    List<AddressBook> findByCreator(User user);
    @Query("SELECT new ru.itis.mailer.dto.AddressBookDto(ab.id, ab.name, ab.state) " +
            "FROM AddressBook ab WHERE ab.creator = :creator")
    List<AddressBookDto> findAddressBooksByCreator(@Param("creator") User creator);
}
