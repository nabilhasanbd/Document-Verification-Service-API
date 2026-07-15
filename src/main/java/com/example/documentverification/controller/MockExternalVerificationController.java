package com.example.documentverification.controller;

import com.example.documentverification.dto.ExternalVerificationRequest;
import com.example.documentverification.dto.ExternalVerificationResult;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Hidden
@RestController
@RequestMapping("/mock-external")
public class MockExternalVerificationController {

    private static final Logger log = LoggerFactory.getLogger(MockExternalVerificationController.class);

    @PostMapping("/verify")
    public ResponseEntity<ExternalVerificationResult> verify(
            @RequestBody ExternalVerificationRequest request) {
        log.info("Mock provider received verification request for document: {}", request.documentId());

        simulateLatency();

        if (ThreadLocalRandom.current().nextDouble() < 0.20) {
            throw new RuntimeException("Mock provider unavailable");
        }

        String referenceId = "MOCK-" + UUID.randomUUID();
        boolean approved = ThreadLocalRandom.current().nextDouble() < 0.85;

        ExternalVerificationResult result = approved
                ? new ExternalVerificationResult("APPROVED", referenceId, "Verification successful")
                : new ExternalVerificationResult("REJECTED", referenceId, "Document rejected");

        return ResponseEntity.ok(result);
    }

    private void simulateLatency() {
        try {
            long delay = ThreadLocalRandom.current().nextLong(200, 801);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
