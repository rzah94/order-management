package org.example.rzah.ordermanagement.mapper;

import org.example.rzah.ordermanagement.domain.ProductEntity;
import org.example.rzah.ordermanagement.dto.CreatedProductRequestDto;
import org.example.rzah.ordermanagement.dto.ProductResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ProductEntity toEntity(CreatedProductRequestDto dto);

    ProductResponseDto toDto(ProductEntity entity);

    List<ProductResponseDto> toDtoList(List<ProductEntity> entity);
}
