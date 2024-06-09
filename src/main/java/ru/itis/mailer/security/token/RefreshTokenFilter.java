package ru.itis.mailer.security.token;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
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
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/logout");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if (refreshToken != null) {
            if (refreshService.refreshCheck(refreshToken)) {
                filterChain.doFilter(request, response);
                return;
            } else {
                Cookie deleteCookie = new Cookie("refreshToken", null);
                deleteCookie.setHttpOnly(true);
                deleteCookie.setSecure(false);
                deleteCookie.setPath("/");
                deleteCookie.setMaxAge(0);
                response.addCookie(deleteCookie);
                refreshService.clearToken(refreshToken);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
