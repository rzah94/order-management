package org.example.rzah.ordermanagement.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatedProductRequestDto {
    @Size(min = 3, max = 200,
            message = "Product name must be between {min} and {max} characters")
    @NotBlank(message = "Product name is required")
    private String name;

    @Size(max = 500, message = "Description must not exceed {max} characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;
}
