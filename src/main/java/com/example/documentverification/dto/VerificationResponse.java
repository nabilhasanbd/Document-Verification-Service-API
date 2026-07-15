package com.example.documentverification.dto;

import java.time.Instant;
import java.util.UUID;

public record VerificationResponse(
        UUID id,
        UUID customerId,
        UUID documentId,
        String status,
        String providerReference,
        String remarks,
        Instant verifiedAt,
        Instant createdAt
) {}
