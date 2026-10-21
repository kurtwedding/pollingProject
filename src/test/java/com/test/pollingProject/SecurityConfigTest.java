package com.test.pollingProject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;

class SecurityConfigTest {
    private final SecurityConfig securityConfig = new SecurityConfig();

    @Test
    void shouldReturnUnauthorizedWhenNotAuthorized() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        MockHttpServletResponse response = new MockHttpServletResponse();

        securityConfig.authenticationEntryPoint()
                .commence(request, response, new InsufficientAuthenticationException("Unauthorized"));

        assertEquals(401, response.getStatus());
    }

    @Test
    void shouldReturnForbiddenWhenAccessDenied() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        MockHttpServletResponse response = new MockHttpServletResponse();

        securityConfig.accessDeniedHandler()
                .handle(request, response, new AccessDeniedException("Forbidden"));

        assertEquals(403, response.getStatus());
    }

}
