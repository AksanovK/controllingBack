package ru.itis.mailer.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import ru.itis.mailer.models.User;

import java.util.List;

public interface RefreshService {
    boolean timeCheck(Jws<Claims> jws, long limit);

    boolean accessCheck(String accessToken);
    boolean refreshCheck(String refreshToken);

    List<String> generateTokens(User user);

}
