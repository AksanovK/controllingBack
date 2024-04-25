package ru.itis.mailer.repositories;

import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.itis.mailer.models.Token;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByRefreshToken(String token);

    void deleteByUser_Id(Long id);

    Optional<Token> findByUser_Id(Long id);
    @Transactional
    void deleteByRefreshToken(String token);
}
