package com.example.documentverification.service.impl;

import com.example.documentverification.dto.request.GeneratePresignedUrlRequest;
import com.example.documentverification.dto.response.DocumentResponse;
import com.example.documentverification.dto.response.PresignedUrlResponse;
import com.example.documentverification.mapper.DocumentMapper;
import com.example.documentverification.repository.DocumentRepository;
import com.example.documentverification.service.DocumentService;
import com.example.documentverification.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final S3Service s3Service;
    
    @Override
    public PresignedUrlResponse generatePresignedUrl(Long customerId, GeneratePresignedUrlRequest request) {
        return null;
    }
    
    @Override
    public DocumentResponse confirmUpload(Long customerId, String documentKey) {
        return null;
    }
    
    @Override
    public DocumentResponse getDocumentById(Long id) {
        return null;
    }
    
    @Override
    public List<DocumentResponse> getDocumentsByCustomerId(Long customerId) {
        return null;
    }
    
    @Override
    public void deleteDocument(Long id) {
    }
}