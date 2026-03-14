package org.example.rzah.ordermanagement.service;

import org.example.rzah.ordermanagement.DataUtils;
import org.example.rzah.ordermanagement.domain.ProductEntity;
import org.example.rzah.ordermanagement.dto.CreatedProductRequestDto;
import org.example.rzah.ordermanagement.dto.ProductPageResponseDto;
import org.example.rzah.ordermanagement.dto.ProductResponseDto;
import org.example.rzah.ordermanagement.exception.ProductAlreadyExistsException;
import org.example.rzah.ordermanagement.exception.ProductNotFoundException;
import org.example.rzah.ordermanagement.mapper.ProductMapper;
import org.example.rzah.ordermanagement.mapper.ProductPageMapper;
import org.example.rzah.ordermanagement.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private ProductPageMapper productPageMapper;
    @InjectMocks
    private ProductService productService;

    @Test
    void getProducts_WithValidPageAndSize_ShouldReturnProductPageDto() {
        // Given
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        List<ProductEntity> productEntities = List.of(
                DataUtils.getLaptopPersisted(),
                DataUtils.getSmartphonePersisted()
        );

        List<ProductResponseDto> expectedProductDtos = List.of(
                DataUtils.getLaptopResponse(),
                DataUtils.getSmartphoneResponse()
        );

        Page<ProductEntity> productPage = new PageImpl<>(productEntities, pageable, 2);
        ProductPageResponseDto expectedPageDto = new ProductPageResponseDto();
        expectedPageDto.setProducts(expectedProductDtos);

        // When
        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productPageMapper.toPageDto(productPage, productMapper)).thenReturn(expectedPageDto);

        // Then
        ProductPageResponseDto result = productService.getProducts(page, size);

        // Verify
        assertThat(result).isEqualTo(expectedPageDto);
        assertThat(result.getProducts()).hasSize(2);
        assertThat(result.getProducts().get(0).getName()).isEqualTo("Laptop");
        assertThat(result.getProducts().get(1).getName()).isEqualTo("Smartphone");

        verify(productRepository).findAll(pageable);
        verify(productPageMapper).toPageDto(productPage, productMapper);
        verifyNoMoreInteractions(productRepository, productPageMapper);
        verifyNoInteractions(productMapper);
    }

    @Test
    void getProducts_WithEmptyResult_ShouldReturnEmptyProductPageDto() {
        // Given
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductEntity> productPage = Page.empty(pageable);
        ProductPageResponseDto expectedPageDto = new ProductPageResponseDto();
        expectedPageDto.setProducts(List.of());

        // When
        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productPageMapper.toPageDto(productPage, productMapper)).thenReturn(expectedPageDto);

        // Then
        ProductPageResponseDto result = productService.getProducts(page, size);

        assertThat(result).isEqualTo(expectedPageDto);
        assertThat(result.getProducts()).isEmpty();

        verify(productRepository).findAll(pageable);
        verify(productPageMapper).toPageDto(productPage, productMapper);

        verifyNoMoreInteractions(productRepository, productPageMapper);
        verifyNoInteractions(productMapper);
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProductResponseDto() {
        // Given
        ProductEntity productEntity = DataUtils.getLaptopPersisted();
        long entityId = productEntity.getId();
        ProductResponseDto expectedProductResponseDto = DataUtils.getLaptopResponse();

        // When
        when(productRepository.findById(entityId)).thenReturn(Optional.of(productEntity));
        when(productMapper.toDto(productEntity)).thenReturn(expectedProductResponseDto);

        // Then
        ProductResponseDto result = productService.getProductById(entityId);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedProductResponseDto);
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedProductResponseDto);
        verify(productRepository).findById(entityId);
        verify(productMapper).toDto(productEntity);
        verifyNoMoreInteractions(productRepository, productMapper);
    }

    @Test
    void getProductById_WhenProductDoesntExist_ShouldThrowProductNotFoundException() {
        // Given
        long nonExistentId = 999L;

        // When
        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> productService.getProductById(nonExistentId))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product " + nonExistentId + " not found");

        verify(productRepository).findById(nonExistentId);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(productMapper);
    }

    @Test
    void saveProduct_WhenProductNameIsUnique_ShouldReturnProductResponseDto() {
        // Given
        ProductEntity productEntity = DataUtils.getLaptopTransient();
        ProductEntity savedProductEntity = DataUtils.getLaptopPersisted();
        CreatedProductRequestDto productDto = DataUtils.getLaptopRequest();
        ProductResponseDto expectedProductResponseDto = DataUtils.getLaptopResponse();

        // When
        when(productMapper.toEntity(productDto)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(savedProductEntity);
        when(productMapper.toDto(savedProductEntity)).thenReturn(expectedProductResponseDto);

        // Then
        ProductResponseDto result = productService.saveProduct(productDto);

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedProductResponseDto);

        verify(productMapper).toEntity(productDto);
        verify(productRepository).save(productEntity);
        verify(productMapper).toDto(savedProductEntity);

        verifyNoMoreInteractions(productMapper, productRepository);
    }

    @Test
    void saveProduct_WhenProductNameAlreadyExists_ShouldThrowProductAlreadyExistsException() {
        // Given
        ProductEntity productEntity = DataUtils.getLaptopTransient();
        CreatedProductRequestDto productDto = DataUtils.getLaptopRequest();

        // When
        when(productMapper.toEntity(productDto)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenThrow(DataIntegrityViolationException.class);

        // Then
        assertThatThrownBy(() -> productService.saveProduct(productDto))
                .isInstanceOf(ProductAlreadyExistsException.class)
                .hasMessage("Product with name " + productDto.getName() + " already exists");

        verify(productMapper).toEntity(productDto);
        verify(productRepository).save(productEntity);

        verifyNoMoreInteractions(productMapper, productRepository);
        verify(productMapper, never()).toDto(any());
    }

}