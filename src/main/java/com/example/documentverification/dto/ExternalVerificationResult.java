package com.example.documentverification.dto;

public record ExternalVerificationResult(
        String status,
        String referenceId,
        String remarks
) {}
