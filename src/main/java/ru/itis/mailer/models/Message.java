package ru.itis.mailer.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.mailer.enums.ContactInfoType;
import ru.itis.mailer.enums.MessageState;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String subject;

    @Column(length = 255)
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", referencedColumnName = "id")
    private MessageTemplate messageTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_book_id", referencedColumnName = "id")
    private AddressBook addressBook;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "messenger")
    private ContactInfoType contactInfoType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "state")
    private MessageState messageState;

    @Column(name = "createdat", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "sentat", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime sentAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
