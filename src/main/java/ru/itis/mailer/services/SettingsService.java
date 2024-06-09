package ru.itis.mailer.services;

import ru.itis.mailer.dto.Settings;
import ru.itis.mailer.dto.UserSettingsDto;
import ru.itis.mailer.models.User;

import java.util.List;

public interface SettingsService {
    void saveSettings(Settings settings);
    String getSubscriptionLink(String messenger);
    List<User> getUsers();
    boolean saveUser(UserSettingsDto userSettingsDto);
}
