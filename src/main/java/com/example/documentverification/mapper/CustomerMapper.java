package com.example.documentverification.mapper;

import com.example.documentverification.dto.request.CreateCustomerRequest;
import com.example.documentverification.dto.response.CustomerResponse;
import com.example.documentverification.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public interface CustomerMapper {
    
    Customer toEntity(CreateCustomerRequest request);
    
    CustomerResponse toResponse(Customer entity);
    
    Customer updateEntity(Customer entity, CreateCustomerRequest request);
}