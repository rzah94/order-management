package org.example.rzah.ordermanagement.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.example.rzah.ordermanagement.dto.CreatedProductRequestDto;
import org.example.rzah.ordermanagement.dto.ProductPageResponseDto;
import org.example.rzah.ordermanagement.dto.ProductResponseDto;
import org.example.rzah.ordermanagement.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@Validated
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ProductPageResponseDto> getProducts(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        ProductPageResponseDto products = productService.getProducts(page, size);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<ProductPageResponseDto> searchProducts(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(required = false) String name
    ) {
        ProductPageResponseDto products = productService.searchProducts(page, size, name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        ProductResponseDto productResponseDto = productService.getProductById(id);
        return ResponseEntity.ok(productResponseDto);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody CreatedProductRequestDto request) {
        ProductResponseDto savedProduct = productService.saveProduct(request);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }
}
