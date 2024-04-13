package ru.itis.mailer.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.mailer.dto.AuthResponse;
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
    public ResponseEntity<AuthResponse> authenticate(@RequestBody UserLoginDto loginUserDto, HttpServletResponse response) {
        AuthResponse authResponse = authenticationService.authenticate(loginUserDto);

        Cookie accessTokenCookie = new Cookie("accessToken", authResponse.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false); //TODO: https - true
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", authResponse.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(28 * 24 * 60 * 60);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(authResponse);
    }
}
