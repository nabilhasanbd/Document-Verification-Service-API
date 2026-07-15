package com.example.documentverification.service;

import com.example.documentverification.dto.ConfirmUploadRequest;
import com.example.documentverification.dto.ConfirmUploadResponse;
import com.example.documentverification.dto.PresignUploadRequest;
import com.example.documentverification.dto.PresignUploadResponse;

import java.util.Optional;
import java.util.UUID;

public interface DocumentService {

    PresignUploadResponse generatePresignedUrl(UUID customerId, PresignUploadRequest request);

    Optional<ConfirmUploadResponse> confirmUpload(UUID customerId, ConfirmUploadRequest request);
}
