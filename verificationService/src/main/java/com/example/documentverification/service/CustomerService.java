package com.example.documentverification.service;

import com.example.documentverification.common.PageResponse;
import com.example.documentverification.dto.CreateCustomerRequest;
import com.example.documentverification.dto.CustomerResponse;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    CustomerResponse createCustomer(CreateCustomerRequest request);

    CustomerResponse getCustomerById(Long id);

    PageResponse<CustomerResponse> getCustomers(Pageable pageable);
}
