package org.example.rzah.ordermanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PaginationInfo {
    private int size;
    private int page;
    private int totalPages;
    private long totalElements;
    private boolean hasNext;
    private boolean hasPrevious;
}