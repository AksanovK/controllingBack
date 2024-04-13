package ru.itis.mailer.services;

import ru.itis.mailer.dto.InstructionsResponse;
import ru.itis.mailer.models.User;

import java.util.List;

public interface InstructionsService {
    InstructionsResponse getInstructions(Long userId);
}
