package com.example.documentverification.dto;

import java.time.Instant;
import java.util.UUID;

public record DocumentResponse(
        String fileName,
        String url,
        String status,
        Instant uploadedAt
) {}
