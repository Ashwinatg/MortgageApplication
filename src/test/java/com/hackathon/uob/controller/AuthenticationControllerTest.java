package com.hackathon.uob.controller;

import com.hackathon.uob.entity.AuthenticationRequest;
import com.hackathon.uob.entity.AuthenticationResponse;
import com.hackathon.uob.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any; // Added for any()
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {AuthenticationController.class})
class AuthenticationControllerTest {

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationController authenticationController;

   // @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
       mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void testLoginSuccess() throws Exception {
        // Given
        AuthenticationRequest request = new AuthenticationRequest("validUsername", "validPassword");
        AuthenticationResponse response = new AuthenticationResponse("token", "validUsername");

        when(authenticationService.login(any(AuthenticationRequest.class))).thenReturn(response); // Changed to any()

        // When and Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"validUsername\", \"password\": \"validPassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"))
                .andExpect(jsonPath("$.username").value("validUsername"));
    }

    @Test
    void testLoginFailure() throws Exception {
        // Given
        AuthenticationRequest request = new AuthenticationRequest("invalidUsername", "invalidPassword");


        when(authenticationService.login(request)).thenThrow(new BadCredentialsException("Invalid username or password"));

        // When and Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"invalidUsername\", \"password\": \"invalidPassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.token").value("error")) // Changed from status to token
                .andExpect(jsonPath("$.username").value("Invalid username or password")); // Changed from message to username
    }
}
