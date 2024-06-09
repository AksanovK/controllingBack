package ru.itis.mailer.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContactInfoType {

    EMAIL("EMAIL"),

    TELEGRAM("TELEGRAM"),

    VK("VK"),

    WHATSAPP("WHATSAPP"),

    DISCORD("DISCORD");

    private final String value;
}
