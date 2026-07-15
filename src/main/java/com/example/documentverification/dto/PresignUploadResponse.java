package com.example.documentverification.dto;

public record PresignUploadResponse(
        String uploadUrl,
        String fileKey,
        long expiresIn
) {}
