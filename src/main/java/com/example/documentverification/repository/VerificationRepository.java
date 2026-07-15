package com.example.documentverification.repository;

import com.example.documentverification.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VerificationRepository extends JpaRepository<Verification, UUID> {

    Optional<Verification> findByDocumentId(UUID documentId);

    Optional<Verification> findFirstByCustomer_IdOrderByCreatedAtDesc(UUID customerId);
}
