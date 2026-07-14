package com.example.documentverification.service;

import com.example.documentverification.dto.request.CreateCustomerRequest;
import com.example.documentverification.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    
    CustomerResponse createCustomer(CreateCustomerRequest request);
    
    CustomerResponse getCustomerById(Long id);
    
    List<CustomerResponse> getAllCustomers();
    
    CustomerResponse updateCustomer(Long id, CreateCustomerRequest request);
    
    void deleteCustomer(Long id);
}