package org.example.rzah.ordermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPageDto {
    private List<ResponseUserDto> users;
    private PaginationInfo paginationInfo;

    @Getter
    @Setter
    public static class PaginationInfo {
        private int size;
        private int page;
        private int totalPages;
        private long totalElements;
        private boolean hasNext;
        private boolean hasPrevious;
    }
}
