package ru.itis.mailer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.mailer.dto.Settings;
import ru.itis.mailer.dto.UserSettingsDto;
import ru.itis.mailer.models.User;
import ru.itis.mailer.services.SettingsService;

import java.util.List;

@RequestMapping("/settings")
@RestController
public class SettingsController {

    @Autowired
    private SettingsService settingsService;
    @PostMapping("/saveSettings")
    public ResponseEntity<?> addSettings(@RequestBody Settings settings) {
        settingsService.saveSettings(settings);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/getSubscriptionLink")
    public ResponseEntity<String> getSubscriptionLink(@RequestParam("messenger") String messenger) {
        String link = settingsService.getSubscriptionLink(messenger);
        if (link != null) {
            return ResponseEntity.ok(link);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Messenger not found");
        }
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(settingsService.getUsers());
    }

    @PostMapping("/updateUser")
    public ResponseEntity<?> addSettings(@RequestBody UserSettingsDto user) {
        boolean result = settingsService.saveUser(user);
        if (result) {
            return ResponseEntity.status(201).body(null);
        } else {
            return ResponseEntity.status(400).body(null);
        }
    }

    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@RequestBody UserSettingsDto user) {
        boolean result = settingsService.saveUser(user);
        if (result) {
            return ResponseEntity.status(201).body(null);
        } else {
            return ResponseEntity.status(400).body(null);
        }
    }
}
