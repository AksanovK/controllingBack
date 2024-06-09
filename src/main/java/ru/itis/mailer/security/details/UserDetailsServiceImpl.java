package ru.itis.mailer.security.details;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.itis.mailer.models.User;
import ru.itis.mailer.repositories.TokenRepository;
import ru.itis.mailer.repositories.UserRepository;

import java.util.function.Supplier;

@Component("tokenUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private TokenRepository tokensRepository;

    @Autowired
    private UserRepository usersRepository;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = usersRepository.findById((int) Long.parseLong(id)).orElseThrow((Supplier<Throwable>) () -> new UsernameNotFoundException("User not found"));
        return new UserDetailsImpl(user);
    }

}
