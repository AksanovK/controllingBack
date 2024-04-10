package ru.itis.mailer.security.token;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itis.mailer.services.RefreshService;

import java.io.IOException;

@Component
public class RefreshTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RefreshService refreshService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException {
        String token = request.getHeader("REFRESH-TOKEN");
        if (token != null) {
            if (refreshService.refreshCheck(token)) {
                filterChain.doFilter(request, response);
            } else {
                response.sendRedirect("/auth");
            }
        }
        return;
    }
}
