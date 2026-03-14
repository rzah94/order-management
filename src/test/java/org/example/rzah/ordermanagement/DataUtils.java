package org.example.rzah.ordermanagement;

import org.example.rzah.ordermanagement.domain.ProductEntity;
import org.example.rzah.ordermanagement.domain.UserEntity;
import org.example.rzah.ordermanagement.dto.CreatedProductRequestDto;
import org.example.rzah.ordermanagement.dto.CreatedUserRequestDto;
import org.example.rzah.ordermanagement.dto.ProductResponseDto;
import org.example.rzah.ordermanagement.dto.UserResponseDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DataUtils {
    private static final String JOHN_DOE_NAME = "John Doe";

    // USERS
    public static UserEntity getJohnDoeTransient() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(JOHN_DOE_NAME);
        userEntity.setEmail("john_doe@gmail.com");
        userEntity.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 11, 12));
        return userEntity;
    }

    public static UserEntity getMikeSmithTransient() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Mike Smith");
        userEntity.setEmail("mike_smith@gmail.com");
        userEntity.setCreatedAt(LocalDateTime.of(2026, 2, 3, 11, 15, 7));
        return userEntity;
    }

    public static UserEntity getJohnDoePersisted() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName(JOHN_DOE_NAME);
        userEntity.setEmail("john_doe@gmail.com");
        userEntity.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 11, 12));
        return userEntity;
    }

    public static UserEntity getMikeSmithPersisted() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(2L);
        userEntity.setName("Mike Smith");
        userEntity.setEmail("mike_smith@gmail.com");
        userEntity.setCreatedAt(LocalDateTime.of(2026, 2, 3, 11, 15, 7));
        return userEntity;
    }

    public static UserResponseDto getJohnDoeResponse() {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setName(JOHN_DOE_NAME);
        userResponseDto.setEmail("john_doe@gmail.com");
        userResponseDto.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 11, 12));
        return userResponseDto;
    }

    public static UserResponseDto getMikeSmithResponse() {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(2L);
        userResponseDto.setName("Mike Smith");
        userResponseDto.setEmail("mike_smith@gmail.com");
        userResponseDto.setCreatedAt(LocalDateTime.of(2026, 2, 3, 11, 15, 7));
        return userResponseDto;
    }

    public static CreatedUserRequestDto getJohnDoeRequestCreated() {
        CreatedUserRequestDto createdUserRequestDto = new CreatedUserRequestDto();
        createdUserRequestDto.setName(JOHN_DOE_NAME);
        createdUserRequestDto.setEmail("john_doe@gmail.com");
        return createdUserRequestDto;
    }

    public static CreatedUserRequestDto getMikeSmithRequestCreated() {
        CreatedUserRequestDto createdUserRequestDto = new CreatedUserRequestDto();
        createdUserRequestDto.setName("Mike Smith");
        createdUserRequestDto.setEmail("mike_smith@gmail.com");
        return createdUserRequestDto;
    }

    // PRODUCTS
    private static final String LAPTOP_NAME = "Laptop";
    private static final String PHONE_NAME = "Smartphone";

    public static ProductEntity getLaptopTransient() {
        ProductEntity entity = new ProductEntity();
        entity.setName(LAPTOP_NAME);
        entity.setDescription("High-performance laptop with 16GB RAM");
        entity.setPrice(new BigDecimal("70000.45"));
        entity.setStockQuantity(6);
        entity.setCreatedAt(LocalDateTime.of(2026, 2, 4, 13, 47, 18));
        return entity;
    }

    public static ProductEntity getSmartphoneTransient() {
        ProductEntity entity = new ProductEntity();
        entity.setName(PHONE_NAME);
        entity.setDescription("Latest smartphone with 5G support");
        entity.setPrice(new BigDecimal("699.99"));
        entity.setStockQuantity(25);
        entity.setCreatedAt(LocalDateTime.of(2026, 2, 5, 9, 30, 0));
        return entity;
    }

    public static ProductEntity getTabletTransient() {
        ProductEntity entity = new ProductEntity();
        entity.setName("Tablet");
        entity.setDescription("10-inch tablet for entertainment");
        entity.setPrice(new BigDecimal("349.99"));
        entity.setStockQuantity(15);
        entity.setCreatedAt(LocalDateTime.of(2026, 2, 6, 14, 20, 45));
        return entity;
    }

    // Persisted (с ID)
    public static ProductEntity getLaptopPersisted() {
        ProductEntity entity = new ProductEntity();
        entity.setId(1L);
        entity.setName(LAPTOP_NAME);
        entity.setDescription("High-performance laptop with 16GB RAM");
        entity.setPrice(new BigDecimal("999.99"));
        entity.setStockQuantity(10);
        entity.setCreatedAt(LocalDateTime.of(2026, 2, 4, 13, 47, 18));
        return entity;
    }

    public static ProductEntity getSmartphonePersisted() {
        ProductEntity entity = new ProductEntity();
        entity.setId(2L);
        entity.setName(PHONE_NAME);
        entity.setDescription("Latest smartphone with 5G support");
        entity.setPrice(new BigDecimal("699.99"));
        entity.setStockQuantity(25);
        entity.setCreatedAt(LocalDateTime.of(2026, 2, 5, 9, 30, 0));
        return entity;
    }

    public static ProductEntity getTabletPersisted() {
        ProductEntity entity = new ProductEntity();
        entity.setId(3L);
        entity.setName("Tablet");
        entity.setDescription("10-inch tablet for entertainment");
        entity.setPrice(new BigDecimal("349.99"));
        entity.setStockQuantity(15);
        entity.setCreatedAt(LocalDateTime.of(2026, 2, 6, 14, 20, 45));
        return entity;
    }

    // Response DTO
    public static ProductResponseDto getLaptopResponse() {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(1L);
        dto.setName(LAPTOP_NAME);
        dto.setDescription("High-performance laptop with 16GB RAM");
        dto.setPrice(new BigDecimal("999.99"));
        dto.setStockQuantity(10);
        dto.setCreatedAt(LocalDateTime.of(2026, 2, 4, 13, 47, 18));
        return dto;
    }

    public static ProductResponseDto getSmartphoneResponse() {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(2L);
        dto.setName(PHONE_NAME);
        dto.setDescription("Latest smartphone with 5G support");
        dto.setPrice(new BigDecimal("699.99"));
        dto.setStockQuantity(25);
        dto.setCreatedAt(LocalDateTime.of(2026, 2, 5, 9, 30, 0));
        return dto;
    }

    public static ProductResponseDto getTabletResponse() {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(3L);
        dto.setName("Tablet");
        dto.setDescription("10-inch tablet for entertainment");
        dto.setPrice(new BigDecimal("349.99"));
        dto.setStockQuantity(15);
        dto.setCreatedAt(LocalDateTime.of(2026, 2, 6, 14, 20, 45));
        return dto;
    }

    // Request DTO (Created)
    public static CreatedProductRequestDto getLaptopRequest() {
        CreatedProductRequestDto dto = new CreatedProductRequestDto();
        dto.setName(LAPTOP_NAME);
        dto.setDescription("High-performance laptop with 16GB RAM");
        dto.setPrice(new BigDecimal("999.99"));
        dto.setStockQuantity(10);
        return dto;
    }

    public static CreatedProductRequestDto getSmartphoneRequest() {
        CreatedProductRequestDto dto = new CreatedProductRequestDto();
        dto.setName(PHONE_NAME);
        dto.setDescription("Latest smartphone with 5G support");
        dto.setPrice(new BigDecimal("699.99"));
        dto.setStockQuantity(25);
        return dto;
    }

    public static CreatedProductRequestDto getTabletRequest() {
        CreatedProductRequestDto dto = new CreatedProductRequestDto();
        dto.setName("Tablet");
        dto.setDescription("10-inch tablet for entertainment");
        dto.setPrice(new BigDecimal("349.99"));
        dto.setStockQuantity(15);
        return dto;
    }
}
