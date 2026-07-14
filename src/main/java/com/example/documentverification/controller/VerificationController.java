package com.example.documentverification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/verifications")
@RequiredArgsConstructor
public class VerificationController {
    
    @PostMapping
    public ResponseEntity<?> initiateVerification() {
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getVerification(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
    
    @GetMapping
    public ResponseEntity<?> getAllVerifications() {
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateVerificationStatus(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}