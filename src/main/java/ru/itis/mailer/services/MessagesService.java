package ru.itis.mailer.services;

import ru.itis.mailer.dto.ContactMessageDto;
import ru.itis.mailer.dto.MessageDto;

import java.util.List;

public interface MessagesService {

    boolean sendCascadeMessage(List<MessageDto> messageDtoList);
    boolean sendMessage(MessageDto messageDto);
    boolean sendContactsMessage(ContactMessageDto contactMessageDto);
    void saveMessage(MessageDto messageDto);
}
