package in.simplygeek.retrospective.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import in.simplygeek.retrospective.constants.CommonConstant;
import in.simplygeek.retrospective.utils.JwtRequestFilter;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtRequestFilter authenticationFilter;

	private static final String[] SWAGGER_UI = { "/", "/error" };

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.cors().configurationSource((HttpServletRequest request) -> {
			CorsConfiguration config = new CorsConfiguration();
			// config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
			config.setAllowedMethods(Collections.singletonList("*"));
			config.setAllowCredentials(true);
			config.setAllowedHeaders(Collections.singletonList("*"));
			config.setMaxAge(3600L);
			return config;
		})
		.and().csrf(c-> c.disable())

				.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/").permitAll().requestMatchers(SWAGGER_UI)
						.permitAll()
						.requestMatchers(HttpMethod.GET, "/api/v1/retrospectives/**").permitAll()
						.requestMatchers("/api/mb/**").hasAuthority(CommonConstant.ADMIN_ROLE)
						.requestMatchers("/**").authenticated())
				.httpBasic(Customizer.withDefaults());
		http.csrf(c-> c.disable());
		return http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class).build();
	}

}