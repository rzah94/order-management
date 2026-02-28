package org.example.rzah.ordermanagement.mapper;

import org.example.rzah.ordermanagement.domain.UserEntity;
import org.example.rzah.ordermanagement.dto.UserPageDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", uses = {UserMapper.class, PaginationMapper.class})
public interface UserPageMapper {
    @Mapping(target = "users", expression = "java(page.getContent().stream().map(userMapper::toDto).toList())")
    @Mapping(target = "paginationInfo", source = "page")
    UserPageDto toPageDto(Page<UserEntity> page, @Context UserMapper userMapper);
}
