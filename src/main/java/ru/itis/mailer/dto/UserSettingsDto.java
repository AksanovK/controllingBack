package ru.itis.mailer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.mailer.enums.UserRole;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSettingsDto {
    private Long id;
    private String email;
    private String password;
    private UserRole role;
    private String firstName;
    private String lastName;
    public  UserSettingsDto(String email, String password, UserRole role, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
