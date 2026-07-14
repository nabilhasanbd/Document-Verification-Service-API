package com.example.documentverification.cache;

import com.example.documentverification.dto.response.VerificationResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public interface VerificationCacheService {
    
    @Cacheable(value = "verifications", key = "#id")
    VerificationResponse getVerification(Long id);
    
    @CacheEvict(value = "verifications", key = "#id")
    void evictVerification(Long id);
    
    @CacheEvict(value = "verifications", allEntries = true)
    void evictAllVerifications();
}