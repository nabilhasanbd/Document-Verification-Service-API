package com.example.documentverification.service.impl;

import com.example.documentverification.dto.request.CreateCustomerRequest;
import com.example.documentverification.dto.response.CustomerResponse;
import com.example.documentverification.mapper.CustomerMapper;
import com.example.documentverification.repository.CustomerRepository;
import com.example.documentverification.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    
    @Override
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        return null;
    }
    
    @Override
    public CustomerResponse getCustomerById(Long id) {
        return null;
    }
    
    @Override
    public List<CustomerResponse> getAllCustomers() {
        return null;
    }
    
    @Override
    public CustomerResponse updateCustomer(Long id, CreateCustomerRequest request) {
        return null;
    }
    
    @Override
    public void deleteCustomer(Long id) {
    }
}