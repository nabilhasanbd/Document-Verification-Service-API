package com.example.documentverification.service;

import com.example.documentverification.dto.VerificationStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationStatusCacheService {

    public static final String CACHE_NAME = "verificationStatus";

    private static final Logger log = LoggerFactory.getLogger(VerificationStatusCacheService.class);

    private final CacheManager cacheManager;

    public VerificationStatusCacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public Optional<VerificationStatusResponse> getStatus(UUID customerId) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) {
            return Optional.empty();
        }
        VerificationStatusResponse value = cache.get(customerId, VerificationStatusResponse.class);
        return Optional.ofNullable(value);
    }

    @CachePut(value = CACHE_NAME, key = "#customerId")
    public VerificationStatusResponse putStatus(UUID customerId, VerificationStatusResponse response) {
        log.debug("Cached verification status for customer {}: {}", customerId, response.status());
        return response;
    }

    @CacheEvict(value = CACHE_NAME, key = "#customerId")
    public void evictStatus(UUID customerId) {
        log.debug("Evicted verification status cache for customer: {}", customerId);
    }
}
