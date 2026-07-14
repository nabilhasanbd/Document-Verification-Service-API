package com.example.documentverification.service;

import com.example.documentverification.dto.request.VerificationRequest;
import com.example.documentverification.dto.response.VerificationResponse;

import java.util.List;

public interface VerificationService {
    
    VerificationResponse initiateVerification(VerificationRequest request);
    
    VerificationResponse getVerificationById(Long id);
    
    List<VerificationResponse> getVerificationsByCustomerId(Long customerId);
    
    VerificationResponse updateVerificationStatus(Long id, String status);
    
    VerificationResponse getVerificationByDocumentId(Long documentId);
}