package com.example.documentverification.controller;

import com.example.documentverification.common.PageResponse;
import com.example.documentverification.dto.CreateCustomerRequest;
import com.example.documentverification.dto.CustomerResponse;
import com.example.documentverification.service.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        log.info("Received request to create customer with email: {}", request.getEmail());
        return customerService.createCustomer(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        log.info("Received request to fetch customer with id: {}", id);
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<CustomerResponse>> getCustomers(Pageable pageable) {
        log.info("Received request to fetch customers - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        PageResponse<CustomerResponse> response = customerService.getCustomers(pageable);
        return ResponseEntity.ok(response);
    }
}
