package ru.itis.mailer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.mailer.security.token.RefreshTokenFilter;

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
