package com.example.documentverification.dto;

public record PresignUploadResponse(
        String uploadUrl,
        String documentId,
        String s3Key
) {}
