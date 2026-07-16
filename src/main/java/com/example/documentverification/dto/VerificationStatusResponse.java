package com.example.documentverification.dto;

import java.util.UUID;

public record VerificationStatusResponse(
        UUID documentId,
        String status
) {}
