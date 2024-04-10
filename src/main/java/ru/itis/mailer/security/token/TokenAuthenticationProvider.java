package ru.itis.mailer.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.itis.mailer.security.details.UserDetailsImpl;

import static ru.itis.mailer.security.token.TokensUtil.getSignInKey;

@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Autowired
    @Qualifier("tokenUserDetailsService")
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
        String token = authentication.getName();
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(getSignInKey(secretKey))
                .build()
                .parseClaimsJws(token);
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(jws.getBody().getSubject());
        tokenAuthentication.setAuthenticated(true);
        tokenAuthentication.setUserDetails(userDetails);
        return tokenAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TokenAuthentication.class.equals(authentication);
    }

}
