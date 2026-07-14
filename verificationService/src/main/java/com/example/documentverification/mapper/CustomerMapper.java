package com.example.documentverification.mapper;

import com.example.documentverification.dto.CreateCustomerRequest;
import com.example.documentverification.dto.CustomerResponse;
import com.example.documentverification.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CreateCustomerRequest request) {
        String[] nameParts = splitName(request.getName());
        return Customer.builder()
                .firstName(nameParts[0])
                .lastName(nameParts[1])
                .email(request.getEmail())
                .phoneNumber(request.getPhone())
                .build();
    }

    public CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(combineName(customer.getFirstName(), customer.getLastName()))
                .email(customer.getEmail())
                .phone(customer.getPhoneNumber())
                .status(customer.getStatus())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }

    private String[] splitName(String name) {
        if (name == null || name.isBlank()) {
            return new String[]{"", ""};
        }
        name = name.trim();
        int spaceIndex = name.indexOf(' ');
        if (spaceIndex < 0) {
            return new String[]{name, ""};
        }
        return new String[]{
                name.substring(0, spaceIndex).trim(),
                name.substring(spaceIndex + 1).trim()
        };
    }

    private String combineName(String firstName, String lastName) {
        if (lastName == null || lastName.isBlank()) {
            return firstName;
        }
        return firstName + " " + lastName;
    }
}
