package ru.itis.mailer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.mailer.dto.UserLoginDto;
import ru.itis.mailer.models.User;
import ru.itis.mailer.services.AuthService;

import java.util.List;

@RequestMapping("/auth")
@RestController
public class LoginController {
    @Autowired
    private AuthService authenticationService;

    @PostMapping
    public ResponseEntity<List<String>> authenticate(@RequestBody UserLoginDto loginUserDto) {
        return ResponseEntity.ok(authenticationService.authenticate(loginUserDto));
    }
}
