package ru.itis.mailer.services;

import ru.itis.mailer.dto.StatusMessage;

public interface SearchingService {
    StatusMessage startParsing(String subject, String type);
    StatusMessage startRanking(String filename);
    StatusMessage startSearching(String filename);
}
