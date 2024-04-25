package ru.itis.mailer.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GenderEnum {

    MALE("MALE"),

    FEMALE("FEMALE"),

    OTHER("OTHER");

    private final String value;
}
