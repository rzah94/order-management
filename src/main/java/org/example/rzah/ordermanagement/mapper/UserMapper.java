package org.example.rzah.ordermanagement.mapper;

import org.example.rzah.ordermanagement.domain.UserEntity;
import org.example.rzah.ordermanagement.dto.RequestCreatedUserDto;
import org.example.rzah.ordermanagement.dto.ResponseUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    UserEntity toEntity(RequestCreatedUserDto dto);

    ResponseUserDto toDto(UserEntity entity);
}
