package ru.itis.mailer.security.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.itis.mailer.models.User;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class TokensUtil {
    private String accessToken;

    private Date accessTime;

    private Date refreshTime;

    private String refreshToken;

    public TokensUtil(User user, long jwtAccessExpiration, long jwtRefreshExpiration, String secretKey) {
        Date date = new Date();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());
        extraClaims.put("email", user.getEmail());
        this.accessToken = Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtAccessExpiration))
                .signWith(getSignInKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
        this.refreshToken = Jwts
                .builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpiration))
                .signWith(getSignInKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
        refreshTime = date;
        accessTime = date;
    }

    public static Key getSignInKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
