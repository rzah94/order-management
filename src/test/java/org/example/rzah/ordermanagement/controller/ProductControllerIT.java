package org.example.rzah.ordermanagement.controller;

import com.jayway.jsonpath.JsonPath;
import org.example.rzah.ordermanagement.DataUtils;
import org.example.rzah.ordermanagement.dto.CreatedProductRequestDto;
import org.example.rzah.ordermanagement.dto.ProductResponseDto;
import org.example.rzah.ordermanagement.repository.ProductRepository;
import org.example.rzah.ordermanagement.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class ProductControllerIT {
    @Container
    public static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_containers")
            .withUsername("test")
            .withPassword("Wtd#hggjur^^guJr");

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void createProduct_WhenProductNameIsUnique_ShouldReturnCreated() throws Exception {
        CreatedProductRequestDto productRequest = DataUtils.getLaptopRequest();
        String requestJson = createProductJson(productRequest);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(productRequest.getName()))
                .andExpect(jsonPath("$.description").value(productRequest.getDescription()))
                .andExpect(jsonPath("$.price").value(productRequest.getPrice()))
                .andExpect(jsonPath("$.stockQuantity").value(productRequest.getStockQuantity()))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void createProduct_WhenProductNameAlreadyExists_ShouldReturnConflict() throws Exception {
        CreatedProductRequestDto productRequest = DataUtils.getLaptopRequest();
        String requestJson = createProductJson(productRequest);

        productService.saveProduct(productRequest);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict());
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnOneProduct() throws Exception {
        ProductResponseDto responseDto = productService.saveProduct(DataUtils.getLaptopRequest());

        mockMvc.perform(get("/products/" + responseDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.name").value(responseDto.getName()))
                .andExpect(jsonPath("$.description").value(responseDto.getDescription()))
                .andExpect(jsonPath("$.price").value(responseDto.getPrice()))
                .andExpect(jsonPath("$.stockQuantity").value(responseDto.getStockQuantity()))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void getProductById_WhenProductDoesntExist_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProducts_WhenThreeProductsExists_ShouldReturnThreeProducts() throws Exception {
        final int page = 0;
        final int size = 3;

        ProductResponseDto LaptopResponseDto = productService.saveProduct(DataUtils.getLaptopRequest());
        ProductResponseDto SmartphoneResponseDto = productService.saveProduct(DataUtils.getSmartphoneRequest());
        ProductResponseDto TabletResponseDto = productService.saveProduct(DataUtils.getTabletRequest());

        MvcResult mvcResult = mockMvc.perform(get(String.format("/products?page=%d&size=%d", page, size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").exists())
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products.length()").value(3))
                .andExpect(jsonPath("$.paginationInfo").exists())
                .andExpect(jsonPath("$.paginationInfo.size").value(size))
                .andExpect(jsonPath("$.paginationInfo.page").value(page))
                .andExpect(jsonPath("$.paginationInfo.totalPages").value(1))
                .andExpect(jsonPath("$.paginationInfo.totalElements").value(3))
                .andExpect(jsonPath("$.paginationInfo.hasNext").value(false))
                .andExpect(jsonPath("$.paginationInfo.hasPrevious").value(false))
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        List<Long> productIds = JsonPath.parse(responseJson)
                .read("$.products[*].id", List.class)
                .stream()
                .map(id -> ((Number) id).longValue())
                .toList();

        assertThat(productIds).containsExactlyInAnyOrder(
                LaptopResponseDto.getId(),
                SmartphoneResponseDto.getId(),
                TabletResponseDto.getId());
    }

    private String createProductJson(CreatedProductRequestDto dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to create JSON", e);
        }
    }
}