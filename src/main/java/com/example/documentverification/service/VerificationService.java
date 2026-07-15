package com.example.documentverification.service;

import com.example.documentverification.dto.VerificationResponse;

import java.util.UUID;

public interface VerificationService {

    VerificationResponse verify(UUID customerId);
}
