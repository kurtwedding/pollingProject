package com.test.pollingProject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Value("${pollingproject.api.key}")
	private String API_KEY;

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return (request, response, authException) -> {
			log.warn("Auth rejected path={} ip={} status={} reason={}",
					request.getRequestURI(),
					request.getRemoteAddr(),
					HttpServletResponse.SC_UNAUTHORIZED,
					authException.getMessage());
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		};
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return (request, response, accessDenied) -> {
			log.warn("Access denied path={} ip={} status={}",
					request.getRequestURI(),
					request.getRemoteAddr(),
					HttpServletResponse.SC_FORBIDDEN);
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		};
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/v3/api-docs/**",
								"/swagger-ui/**",
								"/swagger-ui.html",
								"/webjars/**",
								"/swagger-resources/**")
						.permitAll()
						.requestMatchers("/api/**").authenticated()
						.anyRequest().permitAll())
				.addFilterBefore(new ApiKeyAuthFilter(API_KEY), UsernamePasswordAuthenticationFilter.class);

		http.exceptionHandling(ex -> ex
				.authenticationEntryPoint(authenticationEntryPoint())
				.accessDeniedHandler(accessDeniedHandler()));
		return http.build();
	}
}
