package org.example.rzah.ordermanagement;

import org.example.rzah.ordermanagement.domain.UserEntity;
import org.example.rzah.ordermanagement.dto.CreatedUserRequestDto;
import org.example.rzah.ordermanagement.dto.UserResponseDto;

import java.time.LocalDateTime;

public class DataUtils {
    private static final String JOHN_DOE_NAME = "John Doe";

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
}
