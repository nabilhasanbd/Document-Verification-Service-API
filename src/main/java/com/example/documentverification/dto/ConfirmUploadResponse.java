package com.example.documentverification.dto;

import java.time.Instant;
import java.util.UUID;

public record ConfirmUploadResponse(
        UUID documentId,
        String fileKey,
        String fileName,
        String contentType,
        Long size,
        String status,
        Instant uploadedAt
) {}
