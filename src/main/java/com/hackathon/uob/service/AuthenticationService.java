package com.hackathon.uob.service;

import com.hackathon.uob.entity.AuthenticationRequest;
import com.hackathon.uob.entity.AuthenticationResponse;
import com.hackathon.uob.entity.Customer;
import com.hackathon.uob.repo.CustomerRepository;
import com.hackathon.uob.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
    @Service
    public class AuthenticationService {
        @Autowired
        private AuthenticationManager authenticationManager;

//        @Autowired
//        private CustomUserDetailsService userDetailsService;

        @Autowired
        private JwtTokenUtil jwtTokenUtil;

        @Autowired
        private CustomerRepository customerRepository;


        public AuthenticationResponse login(AuthenticationRequest request) {
            // Validate input
            if (request.getUsername() == null || request.getUsername().isEmpty() ||
                    request.getPassword() == null || request.getPassword().isEmpty()) {
                throw new BadCredentialsException("Username and password are required");
            }

            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Generate JWT token
           // UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            Customer user = customerRepository.findByLoginId(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            UserDetails   userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(user.getLoginId())
                    .password(user.getPassword())
                    .authorities("ROLE_USER")
                    .build();
            String token = jwtTokenUtil.generateToken(userDetails);

            return new AuthenticationResponse(token, request.getUsername());
        }
    }


