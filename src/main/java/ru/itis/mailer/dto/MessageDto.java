package ru.itis.mailer.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.mailer.enums.ContactInfoType;
import ru.itis.mailer.enums.MessageState;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    private Long id;

    private String subject;

    private String body;

    private Long creatorId;

    private Long messageTemplateId;

    private Long addressBookId;

    @Enumerated(EnumType.STRING)
    private ContactInfoType contactInfoType;

    @Enumerated(EnumType.STRING)
    private MessageState messageState;


    public MessageDto(String subject, String body, Long creatorId, Long messageTemplateId, Long addressBookId, ContactInfoType contactInfoType,
                      MessageState messageState) {
        this.subject = subject;
        this.body = body;
        this.creatorId = creatorId;
        this.messageTemplateId = messageTemplateId;
        this.addressBookId = addressBookId;
        this.contactInfoType = contactInfoType;
        this.messageState = messageState;
    }

    public MessageDto(String subject, String body, Long creatorId, Long addressBookId, ContactInfoType contactInfoType, MessageState messageState) {
        this.subject = subject;
        this.body = body;
        this.creatorId = creatorId;
        this.addressBookId = addressBookId;
        this.contactInfoType = contactInfoType;
        this.messageState = messageState;
    }
}
