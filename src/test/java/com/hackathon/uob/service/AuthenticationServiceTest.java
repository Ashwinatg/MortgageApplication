package com.hackathon.uob.service;

import com.hackathon.uob.entity.AuthenticationRequest;
import com.hackathon.uob.entity.AuthenticationResponse;
import com.hackathon.uob.entity.Customer;
import com.hackathon.uob.repo.CustomerRepository;
import com.hackathon.uob.util.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() {
        // Given
        String username = "validUsername";
        String password = "validPassword";
        AuthenticationRequest request = new AuthenticationRequest(username, password);

        Customer customer = new Customer();
        customer.setLoginId(username);
        customer.setPassword(password);

        UserDetails userDetails = User.withUsername(username)
                .password(password)
                .authorities("ROLE_USER")
                .build();
        String token = "mock-jwt-token";

        when(customerRepository.findByLoginId(username)).thenReturn(java.util.Optional.of(customer));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(username, password));
        when(jwtTokenUtil.generateToken(userDetails)).thenReturn(token);

        // When
        AuthenticationResponse response = authenticationService.login(request);

        // Then
        assertNotNull(response);
        assertEquals("validUsername", response.getUsername());
        assertEquals("mock-jwt-token", response.getToken());
    }

    @Test
    void testLoginInvalidCredentials() {
        // Given
        String username = "invalidUsername";
        String password = "invalidPassword";
        AuthenticationRequest request = new AuthenticationRequest(username, password);

        // Simulate invalid credentials exception
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // When & Then
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authenticationService.login(request);
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void testLoginUserNotFound() {
        // Given
        String username = "nonExistentUser";
        String password = "password";
        AuthenticationRequest request = new AuthenticationRequest(username, password);

        when(customerRepository.findByLoginId(username)).thenReturn(java.util.Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            authenticationService.login(request);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testLoginEmptyCredentials() {
        // Given
        String username = "";
        String password = "";
        AuthenticationRequest request = new AuthenticationRequest(username, password);

        // When & Then
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authenticationService.login(request);
        });

        assertEquals("Username and password are required", exception.getMessage());
    }
}
