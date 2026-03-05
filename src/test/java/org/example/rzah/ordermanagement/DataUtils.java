package org.example.rzah.ordermanagement;

import org.example.rzah.ordermanagement.domain.UserEntity;
import org.example.rzah.ordermanagement.dto.RequestCreatedUserDto;
import org.example.rzah.ordermanagement.dto.ResponseUserDto;

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

    public static ResponseUserDto getJohnDoeResponse() {
        ResponseUserDto responseUserDto = new ResponseUserDto();
        responseUserDto.setId(1L);
        responseUserDto.setName(JOHN_DOE_NAME);
        responseUserDto.setEmail("john_doe@gmail.com");
        responseUserDto.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 11, 12));
        return responseUserDto;
    }

    public static ResponseUserDto getMikeSmithResponse() {
        ResponseUserDto responseUserDto = new ResponseUserDto();
        responseUserDto.setId(2L);
        responseUserDto.setName("Mike Smith");
        responseUserDto.setEmail("mike_smith@gmail.com");
        responseUserDto.setCreatedAt(LocalDateTime.of(2026, 2, 3, 11, 15, 7));
        return responseUserDto;
    }

    public static RequestCreatedUserDto getJohnDoeRequestCreated() {
        RequestCreatedUserDto requestCreatedUserDto = new RequestCreatedUserDto();
        requestCreatedUserDto.setName(JOHN_DOE_NAME);
        requestCreatedUserDto.setEmail("john_doe@gmail.com");
        return requestCreatedUserDto;
    }

    public static RequestCreatedUserDto getMikeSmithRequestCreated() {
        RequestCreatedUserDto requestCreatedUserDto = new RequestCreatedUserDto();
        requestCreatedUserDto.setName("Mike Smith");
        requestCreatedUserDto.setEmail("mike_smith@gmail.com");
        return requestCreatedUserDto;
    }
}
