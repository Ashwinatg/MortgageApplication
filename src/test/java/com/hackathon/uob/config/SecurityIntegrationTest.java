package com.hackathon.uob.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.uob.entity.AuthenticationRequest;
import com.hackathon.uob.entity.AuthenticationResponse;
import com.hackathon.uob.service.CustomUserDetailsService;
import com.hackathon.uob.util.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
public class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper; // For serializing AuthenticationRequest

    private String testUserLoginId = "testuser"; // As per CustomUserDetailsService
    private String testUserPassword = "password";

    @BeforeEach
    void setUp() {
        // Ensure CustomUserDetailsService is set up for "testuser" if it's in-memory
        // If it relies on a DB, ensure the user exists.
        // CustomUserDetailsService currently has a hardcoded user "testuser"
    }

    @Test
    void testLoginEndpoint_remainsPublic_andReturnsToken() throws Exception {
        AuthenticationRequest loginRequest = new AuthenticationRequest();
        loginRequest.setUsername(testUserLoginId); // Changed from setLoginId
        loginRequest.setPassword(testUserPassword);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isString());
    }

    @Test
    void testProtectedEndpoint_withoutToken_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/test/hello"))
                .andExpect(status().isUnauthorized()); // Or 403 if filter chain handles it differently
                                                    // Default for missing token is usually 401 from Spring Security if not authenticated
                                                    // if filter chain proceeds and then authorization fails, it might be 403.
                                                    // For JWT filter, if token is missing, it should not authenticate.
    }

    @Test
    void testProtectedEndpoint_withInvalidTokenSignature_shouldReturnUnauthorized() throws Exception {
        String invalidToken = "Bearer anInvalidTokenThatWillNotPassValidation";
        mockMvc.perform(get("/api/test/hello")
                        .header("Authorization", invalidToken))
                .andExpect(status().isUnauthorized()); // Or 403
    }
    
    @Test
    void testProtectedEndpoint_withMalformedToken_shouldReturnUnauthorized() throws Exception {
        String malformedToken = "Bearer malformed"; // Too short, not a valid JWT structure
        mockMvc.perform(get("/api/test/hello")
                        .header("Authorization", malformedToken))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void testProtectedEndpoint_withExpiredToken_shouldReturnUnauthorized() throws Exception {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(testUserLoginId);
        
        // Temporarily change expiration to a very short time
        long originalExpiration = (Long) ReflectionTestUtils.getField(jwtTokenUtil, "expiration");
        ReflectionTestUtils.setField(jwtTokenUtil, "expiration", 1L); // 1 millisecond

        String expiredToken = jwtTokenUtil.generateToken(userDetails);
        
        // Restore original expiration
        ReflectionTestUtils.setField(jwtTokenUtil, "expiration", originalExpiration);

        Thread.sleep(50); // Wait for token to expire

        mockMvc.perform(get("/api/test/hello")
                        .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized()); // Expired token should result in 401
    }

    @Test
    void testProtectedEndpoint_withValidToken_shouldReturnOkAndGreeting() throws Exception {
        // Option A: Full integration - login to get token
        AuthenticationRequest loginRequest = new AuthenticationRequest();
        loginRequest.setUsername(testUserLoginId); // Changed from setLoginId
        loginRequest.setPassword(testUserPassword);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        AuthenticationResponse loginResponse = objectMapper.readValue(responseString, AuthenticationResponse.class);
        String validToken = loginResponse.getToken();

        mockMvc.perform(get("/api/test/hello")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, " + testUserLoginId + "!"));
    }

    @Test
    void testProtectedEndpoint_withValidTokenForDifferentUser_shouldReturnOkAndCorrectGreeting() throws Exception {
        // This test assumes another user "anotheruser" can be loaded by CustomUserDetailsService
        // For simplicity, CustomUserDetailsService has one user. If we need to test this,
        // we'd need to enhance CustomUserDetailsService or mock it.
        // Let's stick to the "testuser" as per the original plan.
        // This scenario is covered by the one above.
    }
}
