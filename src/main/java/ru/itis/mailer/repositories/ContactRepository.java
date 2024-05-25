package ru.itis.mailer.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.mailer.dto.ContactDto;
import ru.itis.mailer.models.AddressBook;
import ru.itis.mailer.models.Contact;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("SELECT new ru.itis.mailer.dto.ContactDto(c.id, c.firstName, c.lastName, c.gender, c.birthday, c.book.id, c.additionalInfo, c.website) " +
            "FROM Contact c WHERE c.book = :book")
    List<ContactDto> customFindAllByBook(@Param("book") AddressBook book);

    List<Contact> findAllByBook(@Param("book") AddressBook book);

    void deleteAllByBook_Id(Long bookId);
    Page<Contact> findByBookId(Long bookId, Pageable pageable);
    Page<Contact> findByFirstNameContainingIgnoreCase(String firstName, Pageable pageable);

    Page<Contact> findByBookIdAndFirstNameContainingIgnoreCase(Long bookId, String firstName, Pageable pageable);
}
