package com.test.pollingProject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
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
				.authenticationEntryPoint((request, response, authException) -> {
					log.warn("Auth rejected path={} ip={} reason={}",
							request.getRequestURI(),
							request.getRemoteAddr(),
							authException.getMessage());
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				})
				.accessDeniedHandler((request, response, accessDenied) -> {
					log.warn("Access denied path={} ip={}",
							request.getRequestURI(),
							request.getRemoteAddr());
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
				}));
		return http.build();
	}
}
