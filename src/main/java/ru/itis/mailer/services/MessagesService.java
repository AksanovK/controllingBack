package ru.itis.mailer.services;

import ru.itis.mailer.dto.MessageDto;

public interface MessagesService {
    void saveMessage(MessageDto messageDto);
}
