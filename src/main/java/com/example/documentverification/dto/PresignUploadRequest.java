package com.example.documentverification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PresignUploadRequest(
        @NotBlank String fileName,
        @NotBlank String contentType,
        @NotNull Long fileSize
) {}
