package com.example.documentverification.mapper;

import com.example.documentverification.dto.response.DocumentResponse;
import com.example.documentverification.entity.Document;
import org.springframework.stereotype.Component;

@Component
public interface DocumentMapper {
    
    DocumentResponse toResponse(Document entity);
    
    Document updateEntity(Document entity);
}