package com.hackathon.uob.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserDetails mockUserDetails;

    @Mock
    private UserDetails mockUserDetailsDifferentUser;

    private final String testSecret = "testSecretKey1234567890123456789012345678901234567890"; // Min 256 bits
    private final Long normalExpiration = 3600000L; // 1 hour
    private final String testUsername = "testuser";
    private final String differentUsername = "otheruser";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "secret", testSecret);
        ReflectionTestUtils.setField(jwtTokenUtil, "expiration", normalExpiration);

        when(mockUserDetails.getUsername()).thenReturn(testUsername);
        when(mockUserDetailsDifferentUser.getUsername()).thenReturn(differentUsername);
    }
    
    @AfterEach
    void tearDown() {
        // Reset expiration to normal in case a test changed it
        ReflectionTestUtils.setField(jwtTokenUtil, "expiration", normalExpiration);
    }

    @Test
    void testGenerateToken() {
        String token = jwtTokenUtil.generateToken(mockUserDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        String token = jwtTokenUtil.generateToken(mockUserDetails);
        String username = jwtTokenUtil.extractUsername(token);
        assertEquals(testUsername, username);
    }

    @Test
    void testValidateToken_Valid() {
        String token = jwtTokenUtil.generateToken(mockUserDetails);
        assertTrue(jwtTokenUtil.validateToken(token, mockUserDetails));
    }

    @Test
    void testValidateToken_InvalidUsername() {
        String token = jwtTokenUtil.generateToken(mockUserDetails);
        assertFalse(jwtTokenUtil.validateToken(token, mockUserDetailsDifferentUser));
    }

    @Test
    void testValidateToken_MalformedToken() {
        String malformedToken = "this.is.not.a.valid.token";

        // First, verify that extractUsername throws MalformedJwtException for a malformed token
        assertThrows(MalformedJwtException.class, () -> {
            jwtTokenUtil.extractUsername(malformedToken);
        }, "extractUsername should throw MalformedJwtException for a malformed token.");

        // Then, verify that validateToken also throws MalformedJwtException for the same reason,
        // as it internally calls extractUsername.
        assertThrows(MalformedJwtException.class, () -> {
            jwtTokenUtil.validateToken(malformedToken, mockUserDetails);
        }, "validateToken should throw MalformedJwtException for a malformed token.");
    }


    @Test
    void testValidateToken_Expired() throws InterruptedException {
        ReflectionTestUtils.setField(jwtTokenUtil, "expiration", 1L); // 1 millisecond
        String token = jwtTokenUtil.generateToken(mockUserDetails);

        // Wait for the token to expire
        Thread.sleep(50); // Sleep for 50ms to ensure token is expired

        final String finalToken = token; // Variable used in lambda should be final or effectively final
        assertThrows(ExpiredJwtException.class, () -> {
            jwtTokenUtil.validateToken(finalToken, mockUserDetails);
        }, "Token should be expired");
        
        // Also test extractUsername for an expired token
         assertThrows(ExpiredJwtException.class, () -> {
            jwtTokenUtil.extractUsername(finalToken);
        }, "Extracting username from expired token should throw ExpiredJwtException");


        // Reset expiration for other tests, though @AfterEach also handles this.
        ReflectionTestUtils.setField(jwtTokenUtil, "expiration", normalExpiration);
    }
    
    @Test
    void testExtractExpiration() {
        long currentTime = System.currentTimeMillis();
        String token = jwtTokenUtil.generateToken(mockUserDetails);
        Date expirationDate = jwtTokenUtil.extractExpiration(token);

        assertNotNull(expirationDate);
        long expectedExpirationTime = currentTime + normalExpiration;

        // Allow a small delta for the time taken to generate the token and execute the code
        long delta = 2000L; // 2 seconds
        assertTrue(Math.abs(expirationDate.getTime() - expectedExpirationTime) < delta,
                "Expiration time should be close to expected. Expected: " + new Date(expectedExpirationTime) + ", Actual: " + expirationDate);
    }
}
