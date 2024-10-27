package com.olympus.service;

import com.cfuv.olympus.service.AuthService;
import com.cfuv.olympus.service.UserService;
import com.cfuv.olympus.web.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;

public class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testLogin() {
//        JwtRequest loginRequest = new JwtRequest();
//        loginRequest.setUsername("testuser");
//        loginRequest.setPassword("password");
//
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("testuser");
//        user.setName("Test");
//        user.setSurname("User");
//        user.setEmail("testuser@example.com");
//        user.setRoles(Set.of(Role.ROLE_PARTICIPANT));
//        user.setSession(1L);
//
//        when(userService.getByUsername("testuser")).thenReturn(user);
//        when(jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRoles()))
//                .thenReturn("test-token");
//
//        JwtResponse response = authService.login(loginRequest);
//
//        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
//        assertEquals("testuser", response.getUsername());
//        assertEquals("test-token", response.getAccessToken());
//    }
//
//    @Test
//    void testCheckAuth() {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(request.getHeader("Authorization")).thenReturn("Bearer test-token");
//        when(jwtTokenProvider.validateToken("test-token")).thenReturn(true);
//        when(jwtTokenProvider.getUsername("test-token")).thenReturn("testuser");
//
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("testuser");
//        user.setName("Test");
//        user.setSurname("User");
//        user.setEmail("testuser@example.com");
//        user.setRoles(Set.of(Role.ROLE_PARTICIPANT));
//        user.setSession(1L);
//
//        when(userService.getByUsername("testuser")).thenReturn(user);
//
//        JwtResponse response = authService.checkAuth(request);
//
//        assertEquals("testuser", response.getUsername());
//        assertEquals("test-token", response.getAccessToken());
//    }
//
//    @Test
//    void testCheckAuthInvalidToken() {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");
//        when(jwtTokenProvider.validateToken("invalid-token")).thenReturn(false);
//
//        JwtResponse response = authService.checkAuth(request);
//
//        assertNull(response);
//    }
//
//    @Test
//    void testCheckAuthUserNotFound() {
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(request.getHeader("Authorization")).thenReturn("Bearer test-token");
//        when(jwtTokenProvider.validateToken("test-token")).thenReturn(true);
//        when(jwtTokenProvider.getUsername("test-token")).thenReturn("unknownuser");
//
//        when(userService.getByUsername("unknownuser")).thenThrow(new UserNotFoundException("User not found"));
//
//        JwtResponse response = authService.checkAuth(request);
//
//        assertNull(response);
//    }
}
