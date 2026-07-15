package com.example.documentverification.dto;

import jakarta.validation.constraints.NotBlank;

public record ConfirmUploadRequest(
        @NotBlank String documentId
) {}
