package ru.itis.mailer.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.mailer.enums.AddressBookState;
import ru.itis.mailer.enums.GenderEnum;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255, name = "first_name")
    private String firstName;

    @Column(nullable = false, length = 255, name = "last_name")
    private String lastName;

    @Column(length = 255, name = "website")
    private String website;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenderEnum gender;

    @Column(name = "birthday")
    private Date birthday;

    @Column(nullable = false, name = "is_foreigner")
    private Boolean isForeigner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private AddressBook book;

    @Column(nullable = false, length = 255)
    private String additionalInfo;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

}
