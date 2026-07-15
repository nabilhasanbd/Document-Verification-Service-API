package com.example.documentverification.controller;

import com.example.documentverification.dto.VerificationResponse;
import com.example.documentverification.dto.VerificationStatusResponse;
import com.example.documentverification.service.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/customers/{customerId}")
@Tag(name = "Verifications", description = "Document verification APIs")
public class VerificationController {

    private static final Logger log = LoggerFactory.getLogger(VerificationController.class);

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify a customer's uploaded document")
    public ResponseEntity<VerificationResponse> verify(@PathVariable UUID customerId) {
        log.info("Received verification request for customer: {}", customerId);
        return ResponseEntity.ok(verificationService.verify(customerId));
    }

    @GetMapping("/verification-status")
    @Operation(summary = "Get the cached verification status for a customer")
    public ResponseEntity<VerificationStatusResponse> getVerificationStatus(@PathVariable UUID customerId) {
        log.info("Received request for verification status of customer: {}", customerId);
        return ResponseEntity.ok(verificationService.getVerificationStatus(customerId));
    }
}
