package ru.itis.mailer.security.token;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itis.mailer.repositories.TokenRepository;
import ru.itis.mailer.repositories.UserRepository;
import ru.itis.mailer.services.RefreshService;


@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    public RefreshService refreshService;

    @Autowired
    public TokenRepository tokensRepository;

    @Autowired
    public UserRepository usersRepository;

    @SneakyThrows
    @Override
    public void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {
        String token = request.getHeader("ACCESS-TOKEN");
        if (token != null) {
            if (refreshService.accessCheck(token)) {
                TokenAuthentication tokenAuthentication = new TokenAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(tokenAuthentication);
                filterChain.doFilter(request, response);
            } else {
                response.sendRedirect("/refresh");
            }
            return;
        }
        filterChain.doFilter(request, response);
    }
}
