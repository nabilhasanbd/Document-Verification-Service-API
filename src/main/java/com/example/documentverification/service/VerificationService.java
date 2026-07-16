package com.example.documentverification.service;

import com.example.documentverification.dto.VerificationResponse;
import com.example.documentverification.dto.VerificationStatusResponse;
import com.example.documentverification.entity.Document;

import java.util.UUID;

public interface VerificationService {

    VerificationStatusResponse initVerification(UUID customerId, Document document);

    VerificationResponse verify(UUID customerId);

    VerificationStatusResponse getVerificationStatus(UUID customerId);
}
