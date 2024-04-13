package ru.itis.mailer.security.token;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itis.mailer.repositories.TokenRepository;

import java.io.IOException;

@Component
public class JwtLogoutFilter extends OncePerRequestFilter {

    @Autowired
    private TokenRepository tokenRepository;

    private final RequestMatcher logoutRequest = new AntPathRequestMatcher("/logout", "GET");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (logoutRequest.matches(request)) {
            System.out.println("LOGOUT FILTER");
            String refresh = request.getHeader("REFRESH-TOKEN");
            tokenRepository.deleteByRefreshToken(refresh);
            SecurityContextHolder.clearContext();
            return;
        }
        filterChain.doFilter(request, response);
        return;
    }
}
