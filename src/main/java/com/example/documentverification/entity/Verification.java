package com.example.documentverification.entity;

import com.example.documentverification.enums.VerificationStatus;
import com.example.documentverification.common.entity.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "verifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Verification extends Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @Column(name = "document_id", nullable = false)
    private Long documentId;
    
    @Column(nullable = false, length = 255)
    private String verificationType;
    
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private VerificationStatus status;
    
    @Column(columnDefinition = "TEXT")
    private String result;
    
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(length = 500)
    private String externalReferenceId;
}