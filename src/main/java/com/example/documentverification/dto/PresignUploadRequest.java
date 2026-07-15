package com.example.documentverification.dto;

import jakarta.validation.constraints.NotBlank;

public record PresignUploadRequest(
        @NotBlank(message = "fileName is required") String fileName
//        @NotBlank(message = "contentType is required") String contentType
) {}
