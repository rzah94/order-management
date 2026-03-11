package org.example.rzah.ordermanagement.service;

import org.example.rzah.ordermanagement.domain.ProductEntity;
import org.example.rzah.ordermanagement.dto.CreatedProductRequestDto;
import org.example.rzah.ordermanagement.dto.ProductPageResponseDto;
import org.example.rzah.ordermanagement.dto.ProductResponseDto;
import org.example.rzah.ordermanagement.exception.ProductAlreadyExistsException;
import org.example.rzah.ordermanagement.exception.ProductNotFoundException;
import org.example.rzah.ordermanagement.mapper.ProductMapper;
import org.example.rzah.ordermanagement.mapper.ProductPageMapper;
import org.example.rzah.ordermanagement.repository.ProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductPageMapper productPageMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, ProductPageMapper productPageMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productPageMapper = productPageMapper;
    }

    public ProductPageResponseDto getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductEntity> products = productRepository.findAll(pageable);
        return productPageMapper.toPageDto(products, productMapper);
    }

    public ProductResponseDto getProductById(Long id) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product " + id + " not found")
        );
        return productMapper.toDto(productEntity);
    }

    @Transactional
    public ProductResponseDto saveProduct(CreatedProductRequestDto productDto) {
        ProductEntity entity = productMapper.toEntity(productDto);
        try {
            ProductEntity savedProduct = productRepository.save(entity);
            return productMapper.toDto(savedProduct);
        } catch (DataIntegrityViolationException e) {
            throw new ProductAlreadyExistsException("Product with name " + productDto.getName() + " already exists");
        }
    }
}
