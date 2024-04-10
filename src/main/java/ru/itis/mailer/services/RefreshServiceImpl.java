package ru.itis.mailer.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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

    @Override
    public List<String> generateTokens(User user) {
        Token refresh_token = tokensRepository.findByUser_Id(user.getId()).orElseThrow(() -> new UsernameNotFoundException(user.getId().toString()));
        TokensUtil tokensUtil = new TokensUtil(user, jwtAccessExpiration, jwtRefreshExpiration, secretKey);
        String new_refresh_token = tokensUtil.getRefreshToken();
        tokensRepository.deleteByRefreshToken(refresh_token.getRefreshToken());
        Token new_token = new Token(new_refresh_token, new Date(), user);
        tokensRepository.save(new_token);
        List<String> list = new ArrayList<>();
        list.add(tokensUtil.getAccessToken());
        list.add(tokensUtil.getRefreshToken());
        return list;
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
