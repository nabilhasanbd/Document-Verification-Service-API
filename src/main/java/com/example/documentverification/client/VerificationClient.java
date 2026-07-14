package com.example.documentverification.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public interface VerificationClient {
    
    @CircuitBreaker(name = "verificationClient", fallbackMethod = "fallbackMethod")
    @Retry(name = "verificationClient")
    @TimeLimiter(name = "verificationClient")
    Mono<String> initiateVerification(String documentData);
    
    default Mono<String> fallbackMethod(String documentData, Throwable throwable) {
        return Mono.just("Fallback response");
    }
}