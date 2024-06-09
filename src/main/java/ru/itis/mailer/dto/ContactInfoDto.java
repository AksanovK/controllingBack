package ru.itis.mailer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.mailer.enums.ContactInfoType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactInfoDto {
    private Long contactId;
    private ContactInfoType type;
    private String value;
}
