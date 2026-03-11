package org.example.rzah.ordermanagement.mapper;

import org.example.rzah.ordermanagement.dto.PaginationInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface PaginationMapper {
    @Mapping(target = "page", expression = "java(page.getNumber())")
    @Mapping(target = "size", expression = "java(page.getSize())")
    @Mapping(target = "totalPages", expression = "java(page.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(page.getTotalElements())")
    @Mapping(target = "hasNext", expression = "java(page.hasNext())")
    @Mapping(target = "hasPrevious", expression = "java(page.hasPrevious())")
    PaginationInfo toPaginationInfo(Page<?> page);
}
