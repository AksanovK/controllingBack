package ru.itis.mailer.controllers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.itis.mailer.models.User;
import ru.itis.mailer.repositories.UserRepository;
import ru.itis.mailer.services.RefreshService;

import java.util.List;

import static ru.itis.mailer.security.token.TokensUtil.getSignInKey;

@RestController
public class RefreshController {

    @Autowired
    public RefreshService refreshService;

    @Autowired
    public UserRepository usersRepository;

    @Transactional
    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(value = "refreshToken", required = false) String requestToken, HttpServletResponse response) {
        if (requestToken != null) {
            List<String> tokens = refreshService.generateTokens(requestToken);
            if (!tokens.isEmpty()) {
                Cookie refreshTokenCookie = new Cookie("refreshToken", tokens.get(1));
                refreshTokenCookie.setHttpOnly(true);
                refreshTokenCookie.setSecure(false);
                refreshTokenCookie.setPath("/");
                refreshTokenCookie.setMaxAge(28 * 24 * 60 * 60);
                response.addCookie(refreshTokenCookie);
            }
            return ResponseEntity.ok(tokens);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Refresh token is missing");
        }
    }
}
