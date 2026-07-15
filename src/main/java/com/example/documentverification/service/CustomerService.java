package com.example.documentverification.service;

import com.example.documentverification.common.PageResponse;
import com.example.documentverification.dto.CreateCustomerRequest;
import com.example.documentverification.dto.CustomerResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CustomerService {

    CustomerResponse createCustomer(CreateCustomerRequest request);

    CustomerResponse getCustomerById(UUID id);

    PageResponse<CustomerResponse> getCustomers(Pageable pageable);
}
