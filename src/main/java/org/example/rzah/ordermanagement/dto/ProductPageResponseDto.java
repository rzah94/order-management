package org.example.rzah.ordermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPageResponseDto {
    private List<ProductResponseDto> products;
    private PaginationInfo paginationInfo;
}
