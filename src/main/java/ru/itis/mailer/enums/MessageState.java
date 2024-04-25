package ru.itis.mailer.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageState {
    SENT("SENT"),
    RECEIVED("RECEIVED"),
    CREATED("CREATED");
    private final String value;
}
