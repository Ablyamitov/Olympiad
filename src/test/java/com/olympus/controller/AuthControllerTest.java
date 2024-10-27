package com.olympus.controller;

import com.cfuv.olympus.service.AuthService;
import com.cfuv.olympus.web.controller.AuthController;
import com.cfuv.olympus.web.dto.auth.JwtResponse;
import com.cfuv.olympus.web.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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

//    @BeforeEach
//    public void setUp() {
//        jwtResponse = new JwtResponse();
//        jwtResponse.setId(1L);
//        jwtResponse.setUsername("testuser");
//        jwtResponse.setAccessToken("test-token");
//
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .addFilters((Filter) new JwtTokenFilter(jwtTokenProvider))
//                .build();
//    }
//
//    @Test
//    public void testCheckAuth() throws Exception {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.addHeader("Authorization", "Bearer test-token");
//
//        Mockito.when(jwtTokenProvider.validateToken("test-token")).thenReturn(true);
//        Mockito.when(jwtTokenProvider.getAuthentication("test-token")).thenReturn(null);
//        Mockito.when(authService.checkAuth(any(HttpServletRequest.class))).thenReturn(jwtResponse);
//
//        mockMvc.perform(get("/api/v1/auth/checkAuth")
//                        .header("Authorization", "Bearer test-token"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("testuser"))
//                .andExpect(jsonPath("$.accessToken").value("test-token"));
//    }
//
//    @Test
//    public void testLogin() throws Exception {
//        JwtRequest loginRequest = new JwtRequest();
//        loginRequest.setUsername("testuser");
//        loginRequest.setPassword("password");
//
//        Mockito.when(authService.login(any(JwtRequest.class))).thenReturn(jwtResponse);
//
//        mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("testuser"))
//                .andExpect(jsonPath("$.accessToken").value("test-token"));
//    }
}
