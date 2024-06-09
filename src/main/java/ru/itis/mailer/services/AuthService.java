package ru.itis.mailer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.mailer.dto.AuthResponse;
import ru.itis.mailer.dto.UserLoginDto;
import ru.itis.mailer.models.Token;
import ru.itis.mailer.models.User;
import ru.itis.mailer.repositories.TokenRepository;
import ru.itis.mailer.repositories.UserRepository;
import ru.itis.mailer.security.token.TokensUtil;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenRepository tokensRepository;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.access-token-expiration-time}")
    private long jwtAccessExpiration;

    @Value("${security.jwt.refresh-token-expiration-time}")
    private long jwtRefreshExpiration;

    public AuthResponse authenticate(UserLoginDto input) {
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow();
        if (passwordEncoder.matches(input.getPassword(), user.getPassword())) {
            Optional<Token> tokenOptional = tokensRepository.findByUser_Id(user.getId());
            tokenOptional.ifPresent(token -> tokensRepository.deleteByRefreshToken(token.getRefreshToken()));
            TokensUtil tokensUtil = new TokensUtil(user, jwtAccessExpiration, jwtRefreshExpiration, secretKey);
            Token token = Token.builder()
                    .refreshToken(tokensUtil.getRefreshToken())
                    .user(user)
                    .time_of_creating(tokensUtil.getRefreshTime())
                    .build();
            tokensRepository.save(token);

            return AuthResponse.
                    builder().
                    accessToken(tokensUtil.getAccessToken()).
                    refreshToken(tokensUtil.getRefreshToken()).
                    userId(user.getId())
                    .role(user.getRole().getAuthority())
                    .build();
        }
        return new AuthResponse();
    }
}
