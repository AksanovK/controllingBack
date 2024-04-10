package ru.itis.mailer.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum UserRole implements GrantedAuthority {
    ADMIN("ADMIN"),
    USER("USER");

    private final String vale;

    @Override
    public String getAuthority() {
        return vale;
    }
}
