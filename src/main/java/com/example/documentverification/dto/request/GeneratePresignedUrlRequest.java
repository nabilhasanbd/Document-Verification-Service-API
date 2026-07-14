package com.example.documentverification.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneratePresignedUrlRequest {
    
    @NotBlank(message = "File name is required")
    @Size(max = 255, message = "File name must not exceed 255 characters")
    private String fileName;
    
    @NotBlank(message = "Document type is required")
    @Size(max = 50, message = "Document type must not exceed 50 characters")
    private String documentType;
    
    @Pattern(regexp = "\\.(jpg|jpeg|png|pdf|doc|docx)$", message = "File type must be jpg, jpeg, png, pdf, doc, or docx")
    private String fileExtension;
    
    @NotBlank(message = "Content type is required")
    private String contentType;
}