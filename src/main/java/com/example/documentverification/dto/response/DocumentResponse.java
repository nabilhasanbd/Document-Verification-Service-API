package com.example.documentverification.dto.response;

import com.example.documentverification.enums.DocumentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {
    
    private Long id;
    
    private Long customerId;
    
    private String fileName;
    
    private String s3Key;
    
    private String bucketName;
    
    private String documentType;
    
    private DocumentStatus status;
    
    private String fileSize;
    
    private String mimeType;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}