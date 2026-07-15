package com.example.documentverification.mapper;

import com.example.documentverification.dto.CreateCustomerRequest;
import com.example.documentverification.dto.CustomerResponse;
import com.example.documentverification.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CreateCustomerRequest request) {
        return Customer.builder()
                .fullName(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();
    }

    public CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}
