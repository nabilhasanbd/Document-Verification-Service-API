package com.example.documentverification.mapper;

import com.example.documentverification.dto.CreateCustomerRequest;
import com.example.documentverification.dto.CustomerResponse;
import com.example.documentverification.dto.DocumentResponse;
import com.example.documentverification.entity.Customer;
import com.example.documentverification.entity.Document;
import com.example.documentverification.service.S3Service;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerMapper {

    private final S3Service s3Service;

    public CustomerMapper(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public Customer toEntity(CreateCustomerRequest request) {
        return Customer.builder()
                .fullName(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();
    }

    public CustomerResponse toResponse(Customer customer) {
        List<DocumentResponse> documents = customer.getDocuments() == null
                ? List.of()
                : customer.getDocuments().stream()
                        .map(this::toDocumentResponse)
                        .toList();

        return CustomerResponse.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .documents(documents)
                .build();
    }

    private DocumentResponse toDocumentResponse(Document document) {
        return new DocumentResponse(
                document.getFileName(),
                s3Service.generatePresignedDownloadUrl(document.getS3Key()),
                document.getStatus(),
                document.getUploadedAt());
    }
}
