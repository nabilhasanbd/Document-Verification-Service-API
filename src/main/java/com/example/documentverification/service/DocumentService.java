package com.example.documentverification.service;

import com.example.documentverification.dto.request.GeneratePresignedUrlRequest;
import com.example.documentverification.dto.response.DocumentResponse;
import com.example.documentverification.dto.response.PresignedUrlResponse;

import java.util.List;

public interface DocumentService {
    
    PresignedUrlResponse generatePresignedUrl(Long customerId, GeneratePresignedUrlRequest request);
    
    DocumentResponse confirmUpload(Long customerId, String documentKey);
    
    DocumentResponse getDocumentById(Long id);
    
    List<DocumentResponse> getDocumentsByCustomerId(Long customerId);
    
    void deleteDocument(Long id);
}