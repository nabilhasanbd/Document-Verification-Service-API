package com.example.documentverification.service.impl;

import com.example.documentverification.dto.ConfirmUploadRequest;
import com.example.documentverification.dto.ConfirmUploadResponse;
import com.example.documentverification.dto.PresignUploadRequest;
import com.example.documentverification.dto.PresignUploadResponse;
import com.example.documentverification.entity.Customer;
import com.example.documentverification.entity.Document;
import com.example.documentverification.entity.DocumentStatus;
import com.example.documentverification.exception.ResourceNotFoundException;
import com.example.documentverification.repository.CustomerRepository;
import com.example.documentverification.repository.DocumentRepository;
import com.example.documentverification.service.DocumentService;
import com.example.documentverification.service.S3Service;
import com.example.documentverification.service.VerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private static final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final DocumentRepository documentRepository;
    private final S3Service s3Service;
    private final VerificationService verificationService;

    public DocumentServiceImpl(CustomerRepository customerRepository,
                               DocumentRepository documentRepository,
                               S3Service s3Service,
                               VerificationService verificationService) {
        this.customerRepository = customerRepository;
        this.documentRepository = documentRepository;
        this.s3Service = s3Service;
        this.verificationService = verificationService;
    }

    @Override
    @Transactional(readOnly = true)
    public PresignUploadResponse generatePresignedUrl(UUID customerId, PresignUploadRequest request) {
        validateCustomerExists(customerId);

        String objectKey = s3Service.generateObjectKey(customerId, request.fileName());
        String uploadUrl = s3Service.generatePresignedUploadUrl(objectKey);
        long expiresIn = s3Service.getPresignedUrlExpirationSeconds();

        log.info("Pre-signed upload URL generated for customer: {}, key: {}", customerId, objectKey);
        return new PresignUploadResponse(uploadUrl, objectKey, expiresIn);
    }

    @Override
    public Optional<ConfirmUploadResponse> confirmUpload(UUID customerId, ConfirmUploadRequest request) {
        validateCustomerExists(customerId);

        String fileKey = request.fileKey();
        HeadObjectResponse head;
        try {
            head = s3Service.headObject(fileKey);
        } catch (NoSuchKeyException e) {
            log.warn("Upload failed - object not found for key: {}", fileKey);
            return Optional.empty();
        }

        Customer customer = customerRepository.getReferenceById(customerId);
        Long size = head.contentLength();
        String contentType = head.contentType();

        Document document = Document.builder()
                .customer(customer)
                .fileName(extractFileName(fileKey))
                .s3Key(fileKey)
                .contentType(contentType)
                .fileSize(size)
                .status(DocumentStatus.UPLOADED.name())
                .uploadedAt(Instant.now())
                .build();
        Document saved = documentRepository.save(document);
        verificationService.initVerification(customerId, saved);

        log.info("Upload confirmed for customer: {}, document: {}", customerId, saved.getId());
        return Optional.of(new ConfirmUploadResponse(
                saved.getId(),
                saved.getS3Key(),
                saved.getFileName(),
                saved.getContentType(),
                saved.getFileSize(),
                saved.getStatus(),
                saved.getUploadedAt()));
    }

    private void validateCustomerExists(UUID customerId) {
        if (!customerRepository.existsById(customerId)) {
            log.warn("Customer not found with id: {}", customerId);
            throw new ResourceNotFoundException("Customer", "id", customerId);
        }
    }

    private String extractFileName(String objectKey) {
        String tail = objectKey.substring(objectKey.lastIndexOf('/') + 1);
        int dash = tail.indexOf('-');
        return dash >= 0 ? tail.substring(dash + 1) : tail;
    }
}
