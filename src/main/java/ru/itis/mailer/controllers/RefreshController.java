package ru.itis.mailer.controllers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.mailer.models.User;
import ru.itis.mailer.repositories.UserRepository;
import ru.itis.mailer.services.RefreshService;

import static ru.itis.mailer.security.token.TokensUtil.getSignInKey;

@RestController
public class RefreshController {

    @Autowired
    public RefreshService refreshService;

    @Autowired
    public UserRepository usersRepository;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader("REFRESH-TOKEN") String requestToken) {
        if (requestToken != null) {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(getSignInKey(secretKey))
                    .build()
                    .parseClaimsJws(requestToken);
            String userId = jws.getBody().getSubject();
            User user = usersRepository.findById((int) Long.parseLong(userId))
                    .orElseThrow(() -> new UsernameNotFoundException(userId));
            return ResponseEntity.ok(refreshService.generateTokens(user));
        } else {
            return ResponseEntity.ok("Bad token");
        }
    }
}
