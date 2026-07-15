package com.example.documentverification.dto;

import jakarta.validation.constraints.NotBlank;

public record ExternalVerificationRequest(
        String customerId,
        String documentId,
        String s3Key,
        String documentType
) {}
