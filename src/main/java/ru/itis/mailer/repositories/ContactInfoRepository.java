package ru.itis.mailer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.mailer.models.ContactInfo;

import java.util.List;

@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {
    List<ContactInfo> findAllByContactId(Long contactId);
    void deleteAllByContact_Id(Long contactId);
}
