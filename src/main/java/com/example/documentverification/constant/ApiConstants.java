package com.example.documentverification.constant;

public interface ApiConstants {
    
    String API_V1 = "/api/v1";
    String CUSTOMERS_ENDPOINT = API_V1 + "/customers";
    String DOCUMENTS_ENDPOINT = API_V1 + "/documents";
    String VERIFICATIONS_ENDPOINT = API_V1 + "/verifications";
    
    String SUCCESS = "SUCCESS";
    String FAILURE = "FAILURE";
    String PENDING = "PENDING";
    
    long DEFAULT_PAGE_SIZE = 20;
    long MAX_PAGE_SIZE = 100;
}