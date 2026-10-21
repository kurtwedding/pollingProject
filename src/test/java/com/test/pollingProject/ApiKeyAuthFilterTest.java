package com.test.pollingProject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

class ApiKeyAuthFilterTest {
    private ApiKeyAuthFilter filter;
    private static final String VALID_API_KEY = "test-api-key";

    @BeforeEach
    void setUp() {
        filter = new ApiKeyAuthFilter(VALID_API_KEY);
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAuthenticateWhenApiKeyProvided() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        request.addHeader("X-API-Key", VALID_API_KEY);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        filter.doFilter(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldReturnUnauthorizedWhenNoApiKeyProvided() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        filter.doFilter(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/swagger-ui",
            "/v3/api-docs",
            "/webjars"
    })
    void shouldAuthenticateToSwaggerDocsWhenNoApiKeyProvided(String swaggerURI) throws ServletException, IOException {
        ApiKeyAuthFilter filter = new ApiKeyAuthFilter("valid-test-key");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(swaggerURI); // Have not provided API Key
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        filter.doFilter(request, response, filterChain);

        assertNotEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
    }
}
