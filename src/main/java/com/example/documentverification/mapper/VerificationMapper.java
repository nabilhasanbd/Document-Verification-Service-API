package com.example.documentverification.mapper;

import com.example.documentverification.dto.response.VerificationResponse;
import com.example.documentverification.entity.Verification;
import org.springframework.stereotype.Component;

@Component
public interface VerificationMapper {
    
    VerificationResponse toResponse(Verification entity);
    
    Verification updateEntity(Verification entity, String status, String result, String errorMessage);
}