package com.example.documentverification.service;

import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import java.util.UUID;

public interface S3Service {

    String generateObjectKey(UUID customerId, String originalFileName);

    String generatePresignedUploadUrl(String objectKey);

    String generatePresignedDownloadUrl(String objectKey);

    long getPresignedUrlExpirationSeconds();

    boolean objectExists(String objectKey);

    HeadObjectResponse headObject(String objectKey);
}
