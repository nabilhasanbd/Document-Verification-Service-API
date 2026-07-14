package com.example.documentverification.entity;

import com.example.documentverification.enums.DocumentStatus;
import com.example.documentverification.common.entity.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document extends Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @Column(nullable = false, length = 255)
    private String fileName;
    
    @Column(nullable = false, length = 500)
    private String s3Key;
    
    @Column(nullable = false, length = 100)
    private String bucketName;
    
    @Column(length = 50)
    private String documentType;
    
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private DocumentStatus status;
    
    @Column(length = 20)
    private String fileSize;
    
    @Column(length = 500)
    private String mimeType;
}