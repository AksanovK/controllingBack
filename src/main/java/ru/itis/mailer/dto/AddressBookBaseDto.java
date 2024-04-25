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
public class AddressBookBaseDto {

    public AddressBookBaseDto(String name, AddressBookState state, String description, Long creatorId) {
        this.name = name;
        this.state = state;
        this.description = description;
        this.creatorId = creatorId;
    }

    private Long id;
    private String name;
    private AddressBookState state;
    private String description;
    private Long creatorId;
}
