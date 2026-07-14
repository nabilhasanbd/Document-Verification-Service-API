package com.example.documentverification.service;

public interface S3Service {
    
    String generatePresignedUrl(String bucketName, String objectKey, long expirationMinutes);
    
    byte[] downloadFile(String bucketName, String objectKey);
    
    void deleteFile(String bucketName, String objectKey);
    
    boolean fileExists(String bucketName, String objectKey);
}