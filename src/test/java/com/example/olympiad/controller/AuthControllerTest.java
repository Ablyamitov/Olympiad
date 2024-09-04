package com.example.olympiad.controller;

import com.example.olympiad.service.AuthService;
import com.example.olympiad.web.controller.AuthController;
import com.example.olympiad.web.dto.auth.JwtRequest;
import com.example.olympiad.web.dto.auth.JwtResponse;
import com.example.olympiad.web.security.JwtTokenFilter;
import com.example.olympiad.web.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    private JwtResponse jwtResponse;

    @BeforeEach
    public void setUp() {
        jwtResponse = new JwtResponse();
        jwtResponse.setId(1L);
        jwtResponse.setUsername("testuser");
        jwtResponse.setAccessToken("test-token");

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters((Filter) new JwtTokenFilter(jwtTokenProvider))
                .build();
    }

    @Test
    public void testCheckAuth() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer test-token");

        Mockito.when(jwtTokenProvider.validateToken("test-token")).thenReturn(true);
        Mockito.when(jwtTokenProvider.getAuthentication("test-token")).thenReturn(null);
        Mockito.when(authService.checkAuth(any(HttpServletRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(get("/api/v1/auth/checkAuth")
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.accessToken").value("test-token"));
    }

    @Test
    public void testLogin() throws Exception {
        JwtRequest loginRequest = new JwtRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        Mockito.when(authService.login(any(JwtRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.accessToken").value("test-token"));
    }
}
