package org.example.rzah.ordermanagement.mapper;

import org.example.rzah.ordermanagement.domain.ProductEntity;
import org.example.rzah.ordermanagement.dto.ProductPageResponseDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, PaginationMapper.class})
public interface ProductPageMapper {
    @Mapping(target = "products", expression = "java(page.getContent().stream().map(productMapper::toDto).toList())")
    @Mapping(target = "paginationInfo", source = "page")
    ProductPageResponseDto toPageDto(Page<ProductEntity> page, @Context ProductMapper productMapper);
}
