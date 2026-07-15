package com.example.documentverification.service.impl;

import com.example.documentverification.exception.S3UploadException;
import com.example.documentverification.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
public class S3ServiceImpl implements S3Service {

    private static final Logger log = LoggerFactory.getLogger(S3ServiceImpl.class);
    private static final Duration PRESIGN_DURATION = Duration.ofMinutes(10);
    private static final long PRESIGN_EXPIRES_SECONDS = 600;

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3ServiceImpl(S3Presigner s3Presigner, S3Client s3Client) {
        this.s3Presigner = s3Presigner;
        this.s3Client = s3Client;
    }

    @Override
    public String generateObjectKey(UUID customerId, String originalFileName) {
        return String.format("customers/%s/documents/%s-%s",
                customerId, UUID.randomUUID(), originalFileName);
    }

    @Override
    public String generatePresignedUploadUrl(String objectKey) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(PRESIGN_DURATION)
                    .putObjectRequest(putObjectRequest)
                    .build();

            PresignedPutObjectRequest presigned = s3Presigner.presignPutObject(presignRequest);
            log.info("Pre-signed URL generated for key: {}", objectKey);
            return presigned.url().toString();
        } catch (S3Exception e) {
            log.error("Failed to generate pre-signed URL for key: {}", objectKey, e);
            throw new S3UploadException("Failed to generate pre-signed upload URL", e);
        }
    }

    @Override
    public long getPresignedUrlExpirationSeconds() {
        return PRESIGN_EXPIRES_SECONDS;
    }

    @Override
    public boolean objectExists(String objectKey) {
        try {
            headObject(objectKey);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    @Override
    public HeadObjectResponse headObject(String objectKey) {
        HeadObjectRequest headRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
        try {
            return s3Client.headObject(headRequest);
        } catch (NoSuchKeyException e) {
            throw e;
        } catch (S3Exception e) {
            log.error("S3 HeadObject failed for key: {}", objectKey, e);
            throw new S3UploadException("Failed to verify object existence in S3", e);
        }
    }
}
