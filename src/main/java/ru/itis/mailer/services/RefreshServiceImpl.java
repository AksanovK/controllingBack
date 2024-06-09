package ru.itis.mailer.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itis.mailer.models.Token;
import ru.itis.mailer.models.User;
import ru.itis.mailer.repositories.TokenRepository;
import ru.itis.mailer.repositories.UserRepository;
import ru.itis.mailer.security.token.TokensUtil;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static ru.itis.mailer.security.token.TokensUtil.getSignInKey;

@Service
public class RefreshServiceImpl implements RefreshService {
    @Autowired
    public TokenRepository tokensRepository;

    @Autowired
    private UserRepository usersRepository;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.access-token-expiration-time}")
    private long jwtAccessExpiration;

    @Value("${security.jwt.refresh-token-expiration-time}")
    private long jwtRefreshExpiration;

    @Override
    public boolean timeCheck(Jws<Claims> jws, long limit) {
        Claims claims = jws.getBody();
        Date time = claims.getExpiration();
        Date nowTime = new Date();
        return (nowTime.getTime() - time.getTime()) <= limit;
    }

    @Override
    public boolean accessCheck(String accessToken) {
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(accessToken);
            return !jws.getBody().getExpiration().before(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    @Override
    public boolean refreshCheck(String refreshToken) {
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(refreshToken);
            if (!jws.getBody().getExpiration().before(new Date())) {
                String userId = jws.getBody().getSubject();
                Optional<Token> token = tokensRepository.findByUser_Id(Long.parseLong(userId));
                if (token.isPresent()) {
                    return token.get().getRefreshToken().equals(refreshToken);
                }
            }
            return false;
        } catch (JwtException e) {
            return false;
        }
    }

    @Transactional
    @Override
    public List<String> generateTokens(String refreshToken) {
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(refreshToken);

        String userId = jws.getBody().getSubject();
        User user = usersRepository.findById((int) Long.parseLong(userId))
                .orElseThrow(() -> new UsernameNotFoundException(userId));
        Optional<Token> existingToken = tokensRepository.findByRefreshToken(refreshToken);
        if (existingToken.isPresent()) {
            tokensRepository.delete(existingToken.get());
            tokensRepository.flush();
        }
        TokensUtil tokensUtil = new TokensUtil(user, jwtAccessExpiration, jwtRefreshExpiration, secretKey);
        String newRefreshToken = tokensUtil.getRefreshToken();
        Token newToken = new Token(newRefreshToken, new Date(), user);
        tokensRepository.save(newToken);
        return List.of(tokensUtil.getAccessToken(), newRefreshToken);
    }

    @Override
    public void clearToken(String token) {
        tokensRepository.deleteByRefreshToken(token);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
