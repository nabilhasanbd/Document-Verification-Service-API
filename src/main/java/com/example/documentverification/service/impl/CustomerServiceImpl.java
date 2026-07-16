package com.example.documentverification.service.impl;

import com.example.documentverification.common.PageResponse;
import com.example.documentverification.dto.CreateCustomerRequest;
import com.example.documentverification.dto.CustomerResponse;
import com.example.documentverification.entity.Customer;
import com.example.documentverification.exception.DuplicateResourceException;
import com.example.documentverification.exception.ResourceNotFoundException;
import com.example.documentverification.mapper.CustomerMapper;
import com.example.documentverification.repository.CustomerRepository;
import com.example.documentverification.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        log.info("Creating customer with email: {}", request.getEmail());

        if (customerRepository.existsByEmail(request.getEmail())) {
            log.warn("Customer creation failed - email already exists: {}", request.getEmail());
            throw new DuplicateResourceException("Customer", "email", request.getEmail());
        }

        Customer customer = customerMapper.toEntity(request);
        Customer savedCustomer = customerRepository.save(customer);

        log.info("Customer created successfully with id: {}", savedCustomer.getId());
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(UUID id) {
        log.info("Fetching customer with id: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Customer not found with id: {}", id);
                    return new ResourceNotFoundException("Customer", "id", id);
                });

        log.info("Customer fetched successfully with id: {}", id);
        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CustomerResponse> getCustomers(Pageable pageable) {
        log.info("Fetching customer list - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Customer> customerPage = customerRepository.findAll(pageable);
        System.err.println("Customer page: " + customerPage.getContent());
        Page<CustomerResponse> responsePage = customerPage.map(customerMapper::toResponse);

        log.info("Customer list fetched - total elements: {}", customerPage.getTotalElements());
        return PageResponse.of(responsePage);
    }
}
