package com.test.pollingProject;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ApiKeyAuthFilter extends OncePerRequestFilter {
    private static final String API_KEY_HEADER = "X-API-Key";
    private String expectedAPIKey; // Loaded from application.properties

    public ApiKeyAuthFilter(String expectedAPIKey) {
        this.expectedAPIKey = expectedAPIKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestApiKey = request.getHeader(API_KEY_HEADER);

        if (expectedAPIKey.equals(requestApiKey)) {
            // Grant access
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken("api-client", null, AuthorityUtils.NO_AUTHORITIES));
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Return a 401 Unauthorized error.
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/webjars");
    }

}
