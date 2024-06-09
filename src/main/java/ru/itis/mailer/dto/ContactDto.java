package ru.itis.mailer.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.mailer.enums.GenderEnum;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactDto {
    private Long id;
    private String firstName;
    private String lastName;
    private GenderEnum gender;
    private Date birthday;
    private Long addressBookId;
    private String additionalInfo;
    private List<ContactInfoDto> contactInfo;
    private String website;


    public ContactDto(String firstName, String lastName, GenderEnum gender, Date birthday, Long addressBookId,
                      String additionalInfo, List<ContactInfoDto> contactInfo, String website) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.addressBookId = addressBookId;
        this.additionalInfo = additionalInfo;
        this.contactInfo = contactInfo;
        this.website = website;
    }

    public ContactDto(Long id, String firstName, String lastName, GenderEnum gender, Date birthday, Long addressBookId,
                      String additionalInfo, String website) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.addressBookId = addressBookId;
        this.additionalInfo = additionalInfo;
        this.website = website;
    }
}
