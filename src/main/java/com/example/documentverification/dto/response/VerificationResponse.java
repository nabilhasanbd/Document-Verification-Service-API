package com.example.documentverification.dto.response;

import com.example.documentverification.enums.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationResponse {
    
    private Long id;
    
    private Long customerId;
    
    private Long documentId;
    
    private String verificationType;
    
    private VerificationStatus status;
    
    private String result;
    
    private String errorMessage;
    
    private String externalReferenceId;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}