package com.example.documentverification.util;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class FileUtils {
    
    public String getFileExtension(String fileName) {
        return null;
    }
    
    public String generateUniqueFileName(String originalName) {
        return null;
    }
    
    public String encodeFileToBase64(byte[] fileData) {
        return Base64.getEncoder().encodeToString(fileData);
    }
    
    public byte[] decodeBase64ToFile(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }
    
    public boolean isValidFileSize(long fileSize, long maxSize) {
        return fileSize <= maxSize;
    }
}