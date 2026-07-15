package com.example.documentverification.service.impl;

import com.example.documentverification.client.ExternalVerificationClient;
import com.example.documentverification.dto.ExternalVerificationRequest;
import com.example.documentverification.dto.ExternalVerificationResult;
import com.example.documentverification.dto.VerificationResponse;
import com.example.documentverification.dto.VerificationStatusResponse;
import com.example.documentverification.entity.Customer;
import com.example.documentverification.entity.Document;
import com.example.documentverification.entity.DocumentStatus;
import com.example.documentverification.entity.Verification;
import com.example.documentverification.entity.VerificationStatus;
import com.example.documentverification.exception.DocumentNotFoundException;
import com.example.documentverification.exception.ResourceNotFoundException;
import com.example.documentverification.repository.CustomerRepository;
import com.example.documentverification.repository.DocumentRepository;
import com.example.documentverification.repository.VerificationRepository;
import com.example.documentverification.service.VerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class VerificationServiceImpl implements VerificationService {

    private static final Logger log = LoggerFactory.getLogger(VerificationServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final DocumentRepository documentRepository;
    private final VerificationRepository verificationRepository;
    private final ExternalVerificationClient externalVerificationClient;

    public VerificationServiceImpl(CustomerRepository customerRepository,
                                   DocumentRepository documentRepository,
                                   VerificationRepository verificationRepository,
                                   ExternalVerificationClient externalVerificationClient) {
        this.customerRepository = customerRepository;
        this.documentRepository = documentRepository;
        this.verificationRepository = verificationRepository;
        this.externalVerificationClient = externalVerificationClient;
    }

    @Override
    @Transactional
    @CacheEvict(value = "verificationStatus", key = "#customerId")
    public VerificationResponse verify(UUID customerId) {
        log.info("Starting verification for customer: {}", customerId);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));

        Document document = customer.getDocuments().stream()
                .filter(d -> DocumentStatus.UPLOADED.name().equals(d.getStatus()))
                .findFirst()
                .orElseThrow(() -> new DocumentNotFoundException("status", DocumentStatus.UPLOADED.name()));

        document.setStatus(DocumentStatus.VERIFICATION_IN_PROGRESS.name());
        documentRepository.save(document);
        log.info("Document {} marked as VERIFICATION_IN_PROGRESS for customer: {}",
                document.getId(), customerId);

        ExternalVerificationRequest request = new ExternalVerificationRequest(
                customerId.toString(),
                document.getId().toString(),
                document.getS3Key(),
                document.getContentType());

        ExternalVerificationResult result = externalVerificationClient.verify(request);
        log.info("Provider responded with status: {} (reference: {}) for document: {}",
                result.status(), result.referenceId(), document.getId());

        VerificationStatus verificationStatus = mapVerificationStatus(result.status());
        boolean completed = verificationStatus == VerificationStatus.VERIFIED
                || verificationStatus == VerificationStatus.REJECTED;

        Verification verification = verificationRepository.findByDocumentId(document.getId())
                .orElseGet(Verification::new);
        verification.setDocument(document);
        verification.setCustomer(customer);
        verification.setStatus(verificationStatus);
        verification.setProviderReference(result.referenceId());
        verification.setRemarks(result.remarks());
        if (completed) {
            verification.setVerifiedAt(Instant.now());
        }
        Verification saved = verificationRepository.save(verification);
        log.info("Verification updated: {} persisted with status: {} for document: {}",
                saved.getId(), saved.getStatus(), document.getId());

        if (completed) {
            document.setStatus(mapDocumentStatus(result.status()).name());
            documentRepository.save(document);
            log.info("Document {} updated to status: {}", document.getId(), document.getStatus());
        } else {
            log.info("Provider unavailable - document {} kept as VERIFICATION_IN_PROGRESS",
                    document.getId());
        }

        log.info("Cache evicted for customer: {} (value: verificationStatus)", customerId);

        return toResponse(saved);
    }

    @Override
    @Cacheable(value = "verificationStatus", key = "#customerId")
    @Transactional(readOnly = true)
    public VerificationStatusResponse getVerificationStatus(UUID customerId) {
        log.info("Cache miss - loading verification status from database for customer: {}", customerId);

        if (!customerRepository.existsById(customerId)) {
            log.warn("Customer not found with id: {}", customerId);
            throw new ResourceNotFoundException("Customer", "id", customerId);
        }

        Verification verification = verificationRepository
                .findFirstByCustomer_IdOrderByCreatedAtDesc(customerId)
                .orElseThrow(() -> {
                    log.warn("Verification not found for customer: {}", customerId);
                    return new ResourceNotFoundException("Verification", "customerId", customerId);
                });

        log.info("Loaded verification status from database for customer: {} (status: {})",
                customerId, verification.getStatus());
        return toStatusResponse(verification);
    }

    private VerificationStatus mapVerificationStatus(String providerStatus) {
        return switch (providerStatus) {
            case "APPROVED" -> VerificationStatus.VERIFIED;
            case "REJECTED" -> VerificationStatus.REJECTED;
            default -> VerificationStatus.FAILED;
        };
    }

    private DocumentStatus mapDocumentStatus(String providerStatus) {
        return switch (providerStatus) {
            case "APPROVED" -> DocumentStatus.VERIFIED;
            case "REJECTED" -> DocumentStatus.REJECTED;
            default -> DocumentStatus.VERIFICATION_IN_PROGRESS;
        };
    }

    private VerificationResponse toResponse(Verification verification) {
        return new VerificationResponse(
                verification.getId(),
                verification.getCustomer().getId(),
                verification.getDocument().getId(),
                verification.getStatus().name(),
                verification.getProviderReference(),
                verification.getRemarks(),
                verification.getVerifiedAt(),
                verification.getCreatedAt());
    }

    private VerificationStatusResponse toStatusResponse(Verification verification) {
        return new VerificationStatusResponse(
                verification.getCustomer().getId(),
                verification.getDocument().getId(),
                verification.getStatus().name(),
                verification.getProviderReference(),
                verification.getRemarks(),
                verification.getVerifiedAt());
    }
}
