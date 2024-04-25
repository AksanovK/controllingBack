package ru.itis.mailer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.mailer.models.Message;
import ru.itis.mailer.models.Token;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
