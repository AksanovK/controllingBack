package ru.itis.mailer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.mailer.dto.Settings;
import ru.itis.mailer.dto.UserSettingsDto;
import ru.itis.mailer.models.User;
import ru.itis.mailer.repositories.UserRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class SettingsServiceImpl implements SettingsService {

    private final String envFilePath = "/var/www/html/messaging-service/.env";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveSettings(Settings settings) {
        LinkedHashMap<String, String> propertiesMap = new LinkedHashMap<>();
        try (FileInputStream input = new FileInputStream(envFilePath)) {
            Properties existingProperties = new Properties();
            existingProperties.load(input);
            for (String key : existingProperties.stringPropertyNames()) {
                propertiesMap.put(key, existingProperties.getProperty(key));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error loading properties file", e);
        }

        propertiesMap.put("VK_TOKEN", settings.getVkToken());
        propertiesMap.put("TELEGRAM_TOKEN", settings.getTelegramToken());
        propertiesMap.put("GMAIL_USER", settings.getGmailUser());
        propertiesMap.put("GMAIL_PASSWORD", settings.getGmailPassword());
        propertiesMap.put("MAYTAPI_API_URL", settings.getMaytapiApiUrl());
        propertiesMap.put("MAYTAPI_TOKEN", settings.getMaytapiToken());
        propertiesMap.put("SUBSCRIBE_VK", settings.getSubscribeVk());
        propertiesMap.put("SUBSCRIBE_WS", settings.getSubscribeWs());
        propertiesMap.put("SUBSCRIBE_TG", settings.getSubscribeTg());

        try (OutputStream output = new FileOutputStream(envFilePath)) {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
            for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("Error saving properties file", e);
        }
    }

    @Override
    public String getSubscriptionLink(String messenger) {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(envFilePath)) {
            properties.load(input);
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }

        return switch (messenger.toLowerCase()) {
            case "vk" -> properties.getProperty("SUBSCRIBE_VK");
            case "whatsapp" -> properties.getProperty("SUBSCRIBE_WS");
            case "telegram" -> properties.getProperty("SUBSCRIBE_TG");
            default -> null;
        };
    }

    @Override
    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public boolean saveUser(UserSettingsDto updatedUser) {
        if (updatedUser.getId() != null) {
            Optional<User> existingUserOptional = userRepository.findById(updatedUser.getId());
            if (existingUserOptional.isPresent()) {
                User existingUser = existingUserOptional.get();
                existingUser.setEmail(updatedUser.getEmail());
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }
                existingUser.setFirstName(updatedUser.getFirstName());
                existingUser.setLastName(updatedUser.getLastName());
                existingUser.setRole(updatedUser.getRole());
                userRepository.save(existingUser);
                return true;
            }
        } else {
            userRepository.save(User.builder().
                    email(updatedUser.getEmail()).
                    password(passwordEncoder.encode(updatedUser.getPassword())).
                    firstName(updatedUser.getFirstName()).
                    lastName(updatedUser.getLastName()).
                    role(updatedUser.getRole()).
                    build());
            return true;
        }
        return false;
    }
}
