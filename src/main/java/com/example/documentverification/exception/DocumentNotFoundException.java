package com.example.documentverification.exception;

public class DocumentNotFoundException extends ResourceNotFoundException {

    public DocumentNotFoundException(String message) {
        super(message);
    }

    public DocumentNotFoundException(String fieldName, Object fieldValue) {
        super("Document", fieldName, fieldValue);
    }
}
