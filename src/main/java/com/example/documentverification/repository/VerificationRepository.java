package com.example.documentverification.repository;

import com.example.documentverification.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {
    
    List<Verification> findByCustomerId(Long customerId);
    
    Optional<Verification> findByDocumentId(Long documentId);
    
    List<Verification> findByStatus(String status);
}