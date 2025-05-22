package com.hackathon.uob.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
    public ResponseEntity<String> hello(Principal principal) {
        // Principal will be null if not authenticated or if endpoint is incorrectly configured
        if (principal == null || principal.getName() == null) {
             // Spring Security typically prevents access before this,
             // but this check is a fallback or for direct unit testing.
             return ResponseEntity.status(401).body("Not authenticated or principal name is null");
        }
        return ResponseEntity.ok("Hello, " + principal.getName() + "!");
    }
}
