package com.example.documentverification.repository;

import com.example.documentverification.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
    List<Document> findByCustomerId(Long customerId);
    
    List<Document> findByCustomerIdAndStatus(Long customerId, String status);
}