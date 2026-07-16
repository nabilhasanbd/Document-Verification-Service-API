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
import com.example.documentverification.service.VerificationStatusCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class VerificationServiceImpl implements VerificationService {

    private static final Logger log = LoggerFactory.getLogger(VerificationServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final DocumentRepository documentRepository;
    private final VerificationRepository verificationRepository;
    private final ExternalVerificationClient externalVerificationClient;
    private final VerificationStatusCacheService verificationStatusCacheService;

    public VerificationServiceImpl(CustomerRepository customerRepository,
                                   DocumentRepository documentRepository,
                                   VerificationRepository verificationRepository,
                                   ExternalVerificationClient externalVerificationClient,
                                   VerificationStatusCacheService verificationStatusCacheService) {
        this.customerRepository = customerRepository;
        this.documentRepository = documentRepository;
        this.verificationRepository = verificationRepository;
        this.externalVerificationClient = externalVerificationClient;
        this.verificationStatusCacheService = verificationStatusCacheService;
    }

    @Override
    public VerificationStatusResponse initVerification(UUID customerId, Document document) {
        log.info("Initializing verification for customer: {}, document: {}", customerId, document.getId());

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));

        Verification verification = Verification.builder()
                .document(document)
                .customer(customer)
                .status(VerificationStatus.PENDING)
                .build();

        Verification saved = verificationRepository.save(verification);
        log.info("Verification created with status {} for customer: {}, document: {}",
                saved.getStatus(), customerId, document.getId());

        VerificationStatusResponse statusResponse = toStatusResponse(saved);
        verificationStatusCacheService.putStatus(customerId, statusResponse);

        return statusResponse;
    }

    @Override
    @Transactional
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

        verificationStatusCacheService.evictStatus(customerId);
        verificationStatusCacheService.putStatus(customerId, toStatusResponse(saved));
        log.info("Cache updated for customer: {} with status: {}", customerId, saved.getStatus());

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public VerificationStatusResponse getVerificationStatus(UUID customerId) {
        if (!customerRepository.existsById(customerId)) {
            log.warn("Customer not found with id: {}", customerId);
            throw new ResourceNotFoundException("Customer", "id", customerId);
        }

        Optional<VerificationStatusResponse> cached = verificationStatusCacheService.getStatus(customerId);
        if (cached.isPresent()) {
            log.info("Cache hit - returning verification status from Redis for customer: {} (status: {})",
                    customerId, cached.get().status());
            return cached.get();
        }

        return loadAndCacheVerificationStatus(customerId);
    }

    private VerificationStatusResponse loadAndCacheVerificationStatus(UUID customerId) {
        log.info("Cache miss - loading verification status from database for customer: {}", customerId);

        Verification verification = verificationRepository
                .findFirstByCustomer_IdOrderByCreatedAtDesc(customerId)
                .orElseThrow(() -> {
                    log.warn("Verification not found for customer: {}", customerId);
                    return new ResourceNotFoundException("Verification", "customerId", customerId);
                });

        VerificationStatusResponse statusResponse = toStatusResponse(verification);
        verificationStatusCacheService.putStatus(customerId, statusResponse);

        log.info("Loaded verification status from database for customer: {} (status: {})",
                customerId, verification.getStatus());
        return statusResponse;
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
                verification.getDocument().getId(),
                verification.getStatus().name());
    }
}
