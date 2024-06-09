package ru.itis.mailer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.mailer.dto.SearchingMessage;
import ru.itis.mailer.security.token.RefreshTokenFilter;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableJpaAuditing
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
