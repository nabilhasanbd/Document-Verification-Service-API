package com.example.documentverification.service.impl;

import com.example.documentverification.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    
    @Override
    public String generatePresignedUrl(String bucketName, String objectKey, long expirationMinutes) {
        return null;
    }
    
    @Override
    public byte[] downloadFile(String bucketName, String objectKey) {
        return null;
    }
    
    @Override
    public void deleteFile(String bucketName, String objectKey) {
    }
    
    @Override
    public boolean fileExists(String bucketName, String objectKey) {
        return false;
    }
}