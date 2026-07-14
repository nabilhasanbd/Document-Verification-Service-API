package com.example.documentverification.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmUploadRequest {
    
    @NotBlank(message = "Document key is required")
    private String documentKey;
    
    @NotBlank(message = "File size is required")
    private String fileSize;
    
    private String mimeType;
}