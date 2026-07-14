package com.example.documentverification.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    
    private List<T> content;
    
    private int currentPage;
    
    private int totalPages;
    
    private long totalElements;
    
    private int pageSize;
    
    private boolean first;
    
    private boolean last;
    
    private boolean empty;
}