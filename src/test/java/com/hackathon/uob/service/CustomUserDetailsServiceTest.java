package com.hackathon.uob.service;

import com.hackathon.uob.entity.Customer;
import com.hackathon.uob.repo.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private static final String VALID_USERNAME = "validUsername";
    private static final String INVALID_USERNAME = "invalidUsername";
    private static final String PASSWORD = "password";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_Success() {
        // Given
        Customer customer = new Customer();
        customer.setLoginId(VALID_USERNAME);
        customer.setPassword(PASSWORD);

        when(customerRepository.findByLoginId(VALID_USERNAME)).thenReturn(java.util.Optional.of(customer));

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(VALID_USERNAME);

        // Then
        assertNotNull(userDetails);
        assertEquals(VALID_USERNAME, userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertEquals("{noop}" + PASSWORD, userDetails.getPassword());  // Password should have {noop} prefix
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Given
        when(customerRepository.findByLoginId(INVALID_USERNAME)).thenReturn(java.util.Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(INVALID_USERNAME);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testLoadUserByUsername_InvalidPassword() {
        // Given
        Customer customer = new Customer();
        customer.setLoginId(VALID_USERNAME);
        customer.setPassword(PASSWORD);

        when(customerRepository.findByLoginId(VALID_USERNAME)).thenReturn(java.util.Optional.of(customer));

        // When
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(VALID_USERNAME);

        // Then
        assertNotNull(userDetails);
        assertEquals("{noop}" + PASSWORD, userDetails.getPassword());  // Ensure password has {noop} prefix
    }
}
