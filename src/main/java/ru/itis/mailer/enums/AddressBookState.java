package ru.itis.mailer.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AddressBookState {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");
    private final String value;
}
