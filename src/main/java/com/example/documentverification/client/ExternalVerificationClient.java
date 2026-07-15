package com.example.documentverification.client;

import com.example.documentverification.dto.ExternalVerificationRequest;
import com.example.documentverification.dto.ExternalVerificationResult;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
public class ExternalVerificationClient {

    private static final Logger log = LoggerFactory.getLogger(ExternalVerificationClient.class);

    private static final Duration TIMEOUT = Duration.ofSeconds(3);

    private final WebClient verificationWebClient;

    public ExternalVerificationClient(WebClient verificationWebClient) {
        this.verificationWebClient = verificationWebClient;
    }

    @CircuitBreaker(name = "default", fallbackMethod = "verifyFallback")
    @Retry(name = "default")
    public ExternalVerificationResult verify(ExternalVerificationRequest request) {
        log.info("Calling external verification provider for document: {}", request.documentId());
        ExternalVerificationResult result = verificationWebClient.post()
                .uri("/mock-external/verify")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ExternalVerificationResult.class)
                .timeout(TIMEOUT)
                .block();
        log.info("External verification provider responded for document: {}", request.documentId());
        return result;
    }

    @SuppressWarnings("unused")
    private ExternalVerificationResult verifyFallback(ExternalVerificationRequest request, Throwable throwable) {
        log.warn("Fallback invoked for document: {} - provider unavailable: {}",
                request.documentId(), throwable.toString());
        return new ExternalVerificationResult("PENDING", null, "Provider unavailable. Retry later.");
    }
}
