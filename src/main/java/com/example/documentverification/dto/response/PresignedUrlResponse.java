package com.example.documentverification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PresignedUrlResponse {
    
    private String uploadUrl;
    
    private String documentKey;
    
    private String bucketName;
    
    private Long expiresInMinutes;
    
    private LocalDateTime expiresAt;
}