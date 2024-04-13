package ru.itis.mailer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.mailer.security.token.RefreshTokenFilter;
import ru.itis.mailer.security.token.TokenAuthenticationProvider;

@SpringBootApplication
public class MailerApplication {

	@Autowired
	private final RefreshTokenFilter refreshTokenFilter;
	public MailerApplication(RefreshTokenFilter refreshTokenFilter) {
		this.refreshTokenFilter = refreshTokenFilter;
	}

	@Bean
	FilterRegistrationBean<RefreshTokenFilter> registrationBean() {
		final FilterRegistrationBean<RefreshTokenFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
		filterFilterRegistrationBean.setFilter(this.refreshTokenFilter);
		filterFilterRegistrationBean.addUrlPatterns("/refresh");
		filterFilterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return filterFilterRegistrationBean;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(MailerApplication.class, args);
	}

}
