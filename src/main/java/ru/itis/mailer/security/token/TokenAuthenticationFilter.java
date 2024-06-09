package ru.itis.mailer.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itis.mailer.repositories.TokenRepository;
import ru.itis.mailer.repositories.UserRepository;
import ru.itis.mailer.security.details.UserDetailsImpl;
import ru.itis.mailer.services.RefreshService;

import java.util.logging.Logger;

import static ru.itis.mailer.security.token.TokensUtil.getSignInKey;


@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    public RefreshService refreshService;

    @Autowired
    public TokenRepository tokensRepository;

    @Autowired
    public UserRepository usersRepository;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Autowired
    @Qualifier("tokenUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenAuthenticationProvider tokenAuthenticationProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/api/logout") || path.startsWith("/api/refresh");
    }

    @SneakyThrows
    @Override
    public void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {
        String token = request.getHeader("ACCESS-TOKEN");
        if (token != null) {
            if (refreshService.accessCheck(token)) {
                TokenAuthentication tokenAuthentication = (TokenAuthentication) tokenAuthenticationProvider.authenticate(new TokenAuthentication(token));
                SecurityContextHolder.getContext().setAuthentication(tokenAuthentication);
                filterChain.doFilter(request, response);
                return;
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Refresh");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
