package com.example.documentverification.controller;

import com.example.documentverification.dto.ConfirmUploadRequest;
import com.example.documentverification.dto.ConfirmUploadResponse;
import com.example.documentverification.dto.PresignUploadRequest;
import com.example.documentverification.dto.PresignUploadResponse;
import com.example.documentverification.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers/{customerId}/documents")
@Tag(name = "Documents", description = "Document upload APIs using S3 pre-signed URLs")
public class DocumentController {

    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/presigned-url")
    @Operation(summary = "Generate a pre-signed S3 upload URL for a document")
    public ResponseEntity<PresignUploadResponse> generatePresignedUrl(
            @PathVariable UUID customerId,
            @Valid @RequestBody PresignUploadRequest request) {
        log.info("Received request to generate pre-signed URL for customer: {}", customerId);
        PresignUploadResponse response = documentService.generatePresignedUrl(customerId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm-upload")
    @Operation(summary = "Confirm a document upload by verifying the object in S3")
    public ResponseEntity<?> confirmUpload(
            @PathVariable UUID customerId,
            @Valid @RequestBody ConfirmUploadRequest request) {
        log.info("Received request to confirm upload for customer: {}", customerId);
        return documentService.confirmUpload(customerId, request)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest()
                        .body(Map.of("message", "Uploaded object not found in S3 for key: " + request.fileKey())));
    }
}
