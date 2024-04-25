package ru.itis.mailer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.mailer.enums.AddressBookState;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressBookDto {
    private Long id;
    private String name;
    private AddressBookState state;
}
