package ru.itis.mailer.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.mailer.enums.ContactInfoType;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactMessageDto {
    private String body;
    private Long creatorId;
    @Enumerated(EnumType.STRING)
    private List<ContactInfoType> contactInfo;
    private List<Long> contacts;
}
